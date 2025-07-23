package shop.dto.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPasswordDto {
	
	@NotBlank
	private String token;
	@Size(min = 8, max=25, message = "Password must be between 8 and 25 characters.")
	private String password;
	@NotBlank(message="Confirmed password required.")
	private String passwordConfirm;
	
	public boolean passwordsAreEqual() {
		return password.equals(passwordConfirm);
	}
}
