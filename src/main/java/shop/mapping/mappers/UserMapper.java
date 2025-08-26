package shop.mapping.mappers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import shop.dto.RegisterUserRequest;
import shop.persistence.entities.User;

@Component
public class UserMapper {
	
	public User toUser(RegisterUserRequest dto) {
		User entity = new User();
		
		entity.setUsername(dto.getUsername());
		entity.setEnabled(false);
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		
		return entity;
	}

	public User toUser(UserDetails userDetails) {
		User entity = new User();
		
		entity.setUsername(userDetails.getUsername());
		entity.setPassword(userDetails.getPassword());
//		entity.setAuthorities(userDetails.getAuthorities()); 
		
		return entity;
	}
}
