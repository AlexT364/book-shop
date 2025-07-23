package shop.messaging.email.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmailPasswordResetPayload implements Serializable{
	
	private String toEmail;
	private String passwordResetToken;
}
