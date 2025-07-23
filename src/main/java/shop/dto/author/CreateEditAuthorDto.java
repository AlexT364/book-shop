package shop.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEditAuthorDto {
	
	private Long id;
	@NotBlank(message = "First name required")
	private String firstName;
	@NotBlank(message = "Last name required")
	private String lastName;
	private String bio;
}
