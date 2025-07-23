package shop.security;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import shop.exceptions.user.UserAlreadyExistsException;
import shop.persistence.entities.User;
import shop.persistence.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager{

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User with username: %s Not Found.".formatted(username)));
		return new SecurityUser(user);
	}

	@Override
	@Transactional
	public void createUser(UserDetails user) {
		if(this.userExists(user.getUsername())) {
			throw new UserAlreadyExistsException("Username %s already in use".formatted(user.getUsername()));
		}
		User userToCreate = modelMapper.map(user, User.class);
		userRepository.save(userToCreate);
		
	}

	@Override
	public void updateUser(UserDetails user) {
		
	}

	@Override
	public void deleteUser(String username) {
		User userToDelete = userRepository
				.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("User with username: %s Not Found.".formatted(username)));
		userRepository.delete(userToDelete);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		UserDetails currentUser = getCurrentUser();
		
		if(!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
			throw new IllegalArgumentException("Old password doesn't match");
		}
		
		User user = userRepository
				.findByUsername(currentUser.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User with username: %s Not Found.".formatted(currentUser.getUsername())));
		
		String newEncodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(newEncodedPassword);
		userRepository.save(user);
	}

	@Override
	public boolean userExists(String username) {
		return userRepository.existsByUsername(username);
	}
	
	private UserDetails getCurrentUser() {
		return (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
