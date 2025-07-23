package shop.services.email;

public interface EmailService {
	
	void sendVerificationEmail(String toEmail, String confirmationToken);
	
	void sendContactReply(String toEmail, String subject, String messageText);
	
	void sendPasswordChangeLink(String toEmail, String passwordResetToken);
}
