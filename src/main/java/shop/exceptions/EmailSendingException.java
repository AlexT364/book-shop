package shop.exceptions;

public class EmailSendingException extends RuntimeException{

	public EmailSendingException() {
		super();
	}

	public EmailSendingException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailSendingException(String message) {
		super(message);
	}

	public EmailSendingException(Throwable cause) {
		super(cause);
	}

}
