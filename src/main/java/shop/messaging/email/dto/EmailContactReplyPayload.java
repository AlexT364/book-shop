package shop.messaging.email.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmailContactReplyPayload implements Serializable{
	
	private String toEmail;
	private String subject;
	private String message;
}
