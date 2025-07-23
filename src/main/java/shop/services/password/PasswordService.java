package shop.services.password;

import shop.dto.password.NewPasswordDto;

public interface PasswordService {
	
	public void sendPasswordResetLink(String email);

	public void verifyToken(String token);

	public void updatePassword(NewPasswordDto passwordResetRequest);
}	
