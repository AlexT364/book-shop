package shop.dto.contact;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortContactMessageDto {

	private Long id;
	private String name;
	private String email;
	private String subject;
	private LocalDateTime addedAt;
	private boolean viewed;
	
}
