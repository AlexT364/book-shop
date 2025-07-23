package shop.services.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import shop.exceptions.EmailSendingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	private static final String REGISTRATION_CONFIRMATION_SUBJECT = "Book shop registration confirmation.";
	private static final String PASSWORD_RESET_SUBJECT = "Password reset.";
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Value("${app.baseUrl}")
	private String baseUrl;

	@Override
	public void sendVerificationEmail(String email, String confirmationToken) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		String confirmationLink = this.baseUrl + "registration/confirm?token=%s".formatted(confirmationToken);
		
		try {
			helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(email);
			helper.setFrom(this.fromEmail);
			
			Context context = new Context();
			context.setVariable("confirmationLink", confirmationLink);
			String html = templateEngine.process("messages/confirmation-email", context);
			
			helper.setSubject(REGISTRATION_CONFIRMATION_SUBJECT);
			helper.setText(html, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new EmailSendingException(e);
		}

	}

	@Override
	public void sendContactReply(String toEmail, String subject, String messageText) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(toEmail);
			helper.setFrom(this.fromEmail);
			
			Context context = new Context();
			context.setVariable("messageText", messageText);
			String html = templateEngine.process("messages/contact-reply-email.html", context);
			
			helper.setSubject(subject);
			helper.setText(html, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new EmailSendingException(e);
		}
	}

	@Override
	public void sendPasswordChangeLink(String toEmail, String passwordResetToken) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		String passwordResetLink = this.baseUrl + "password/reset?token=%s".formatted(passwordResetToken);
		
		try {
			helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(toEmail);
			helper.setFrom(this.fromEmail);
			
			Context context = new Context();
			context.setVariable("passwordResetLink", passwordResetLink);
			String html = templateEngine.process("messages/password-reset-email.html", context);
			
			helper.setSubject(PASSWORD_RESET_SUBJECT);
			helper.setText(html, true);
			
			mailSender.send(mimeMessage);
		}catch(MessagingException e) {
			throw new EmailSendingException(e);
		}
		
	}
}
