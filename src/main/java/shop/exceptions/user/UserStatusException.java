package shop.exceptions.user;

public class UserStatusException extends RuntimeException{

	public UserStatusException() {
		super();
	}

	public UserStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserStatusException(String message) {
		super(message);
	}
	
}
