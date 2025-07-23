package shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
	
	@NotBlank(message = "Username required.")
	@Size(min = 2, max=25, message = "Username length should be at least 2 characters.")
	private String username;
	@NotBlank(message = "Email address required.")
	@Email
	private String email;
	@NotBlank(message="Password required")
	@Pattern(regexp = "^(?=.*?[A-Z]).{8,}$",
			message = "Password should contain at least one uppercase letter.")
	@Size(min = 8, max=25, message = "Password must be between 8 and 25 characters.")
	private String password;
	@NotBlank(message="Confirmed password required.")
	private String passwordConfirm;
	
	public boolean passwordsAreEqual() {
		return password.equals(passwordConfirm);
	}
}
