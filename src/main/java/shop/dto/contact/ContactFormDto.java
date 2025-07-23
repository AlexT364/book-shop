package shop.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDto {
	@NotBlank(message = "Name is required.")
	@Size(min = 2, max = 50)
	private String name;
	@NotBlank(message="Email is required.")
	@Email
	@Size(max = 254)
	private String email;
	@Size(max = 40, message = "Phone number length cannot be longer than 40.")
	private String phone;
	@Size(max = 254, message = "Subject's length cannot be longer than 254.")
	private String subject;
	@NotBlank(message = "Message is required.")
	private String message;
}
