package shop.messaging.email.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmailConfirmationPayload implements Serializable{
	
	private String toEmail;
	private String confirmationToken;
}
