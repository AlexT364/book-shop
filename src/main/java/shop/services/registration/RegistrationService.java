package shop.services.registration;

import shop.dto.RegisterUserRequest;

public interface RegistrationService {
	
	public void registerUser(RegisterUserRequest userRegistrationRequest);
	
	public void confirmUser(String token);

	public void resendConfirmationToken(String username);
}
