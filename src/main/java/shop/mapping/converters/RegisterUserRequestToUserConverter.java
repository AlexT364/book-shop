package shop.mapping.converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.RegisterUserRequest;
import shop.persistence.entities.User;

public class RegisterUserRequestToUserConverter implements Converter<RegisterUserRequest, User> {

	@Override
	public User convert(MappingContext<RegisterUserRequest, User> context) {
		RegisterUserRequest dto = context.getSource();
		User entity = new User();
		
		entity.setUsername(dto.getUsername());
		entity.setEnabled(false);
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		
		return entity;
	}

}