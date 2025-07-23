package shop.dto.contact;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReplyFormDto {
	
	private String subject;
	@Size(min = 10, message = "Reply message must contain at least 10 symbols.")
	private String message;

}
