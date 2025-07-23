package shop.dto.contact;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContactMessageDto {

	private Long id;
	private String name;
	private String email;
	private String phone;
	private String subject;
	private String message;
	private LocalDateTime addedAt;
	private boolean viewed; 
	
}
