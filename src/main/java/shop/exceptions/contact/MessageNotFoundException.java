package shop.exceptions.contact;

public class MessageNotFoundException extends RuntimeException{

	public MessageNotFoundException() {
		super();
	}

	public MessageNotFoundException(String message) {
		super(message);
	}

}
