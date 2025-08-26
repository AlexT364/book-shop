package shop.services.registration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.RegisterUserRequest;
import shop.exceptions.security.TokenExpiredException;
import shop.exceptions.user.UserAlreadyExistsException;
import shop.exceptions.user.UserNotFoundException;
import shop.exceptions.user.UserStatusException;
import shop.mapping.mappers.UserMapper;
import shop.messaging.email.dto.EmailConfirmationPayload;
import shop.messaging.email.producer.EmailConfirmationSender;
import shop.persistence.entities.Authority;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.AuthorityPK;
import shop.persistence.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailConfirmationSender emailConfirmationSender;
	private final UserMapper userMapper;
	private static final String DEFAULT_ROLE = "ROLE_USER";

	private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000;// 24 hours

	@Override
	@Transactional
	public void registerUser(RegisterUserRequest registrationRequest) {
		if (userRepository.existsByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail())) {
			throw new UserAlreadyExistsException("Username or email already exists.");
		}
		
		User userToCreate = userMapper.toUser(registrationRequest);
		userToCreate.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userToCreate.setEnabled(false);

		String confirmationToken = UUID.randomUUID().toString();
		userToCreate.setConfirmationToken(confirmationToken);
		userToCreate.setTokenCreationDate(LocalDateTime.now());

		Authority authority = new Authority();
		authority.setPk(new AuthorityPK(userToCreate, DEFAULT_ROLE));
		userToCreate.setAuthorities(List.of(authority));

		User newUser = userRepository.save(userToCreate);

		EmailConfirmationPayload confirmationPayload = new EmailConfirmationPayload();
		confirmationPayload.setToEmail(newUser.getEmail());
		confirmationPayload.setConfirmationToken(confirmationToken);
		
		this.emailConfirmationSender.sendConfirmationToken(confirmationPayload);

	}

	@Override
	public void confirmUser(String token) {
		User user = userRepository.findByConfirmationToken(token)
				.orElseThrow(() -> new UserNotFoundException("User not found."));

		LocalDateTime now = LocalDateTime.now();

		long tokenAge = Duration.between(user.getTokenCreationDate(), now).toMillis();
		if (tokenAge < TOKEN_EXPIRATION_TIME) {
			user.setEnabled(true);
			user.setConfirmationToken(null);
			user.setTokenCreationDate(null);
			userRepository.save(user);
		} else {
			this.resendConfirmationToken(user.getUsername());
			throw new TokenExpiredException("Verification token expired.");
		}
	}

	@Override
	@Transactional
	public void resendConfirmationToken(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("Invalid request."));

		if (user.isEnabled()) {
			throw new UserStatusException("Invalid request.");
		}

		String confirmationToken = UUID.randomUUID().toString();
		user.setConfirmationToken(confirmationToken);
		user.setTokenCreationDate(LocalDateTime.now());
		
		EmailConfirmationPayload confirmationPayload = new EmailConfirmationPayload();
		confirmationPayload.setToEmail(user.getEmail());
		confirmationPayload.setConfirmationToken(confirmationToken);
		
		this.emailConfirmationSender.sendConfirmationToken(confirmationPayload);

		userRepository.save(user);
	}

}
