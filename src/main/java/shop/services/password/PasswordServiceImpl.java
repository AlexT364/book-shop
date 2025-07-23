package shop.services.password;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.password.NewPasswordDto;
import shop.exceptions.security.InvalidTokenException;
import shop.messaging.email.dto.EmailPasswordResetPayload;
import shop.messaging.email.producer.EmailPasswordResetSender;
import shop.persistence.entities.PasswordResetToken;
import shop.persistence.entities.User;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.password.PasswordResetTokenRepository;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService{
	
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final UserRepository userRepository;
	private final EmailPasswordResetSender passwordResetSender;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void sendPasswordResetLink(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			System.err.println("NOT FOUND");
			return;
		}
		User user = userOptional.get();
		
		PasswordResetToken passResetToken = new PasswordResetToken();
		passResetToken.setCreatedAt(LocalDateTime.now());
		passResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
		passResetToken.setToken(UUID.randomUUID().toString());
		passResetToken.setUser(user);
		
		passResetToken = passwordResetTokenRepository.save(passResetToken);
		
		EmailPasswordResetPayload passResetPayload = new EmailPasswordResetPayload();
		passResetPayload.setToEmail(email);
		passResetPayload.setPasswordResetToken(passResetToken.getToken());
		
		this.passwordResetSender.sendPasswordResetToken(passResetPayload);
	}

	@Override
	public void verifyToken(String token) {
		PasswordResetToken tokenEntity = passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("The reset link is invalid or expired."));
		if(tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException("The reset link is invalid or expired.");
		}
	}

	@Override
	@Transactional
	public void updatePassword(NewPasswordDto newPasswordDto) {
		PasswordResetToken tokenEntity = passwordResetTokenRepository.findByTokenWithUser(newPasswordDto.getToken())
				.orElseThrow(() -> new InvalidTokenException("The reset link is invalid or expired."));
		if(tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
			passwordResetTokenRepository.delete(tokenEntity);
			throw new InvalidTokenException("The reset link is invalid or expired.");
		}
		
		User user = tokenEntity.getUser();
		user.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
		
		passwordResetTokenRepository.delete(tokenEntity);
		userRepository.save(user);
	}
	
	

	
}






































