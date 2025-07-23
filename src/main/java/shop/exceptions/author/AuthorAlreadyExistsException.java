package shop.exceptions.author;

public class AuthorAlreadyExistsException extends RuntimeException{

	public AuthorAlreadyExistsException() {
		super();
	}

	public AuthorAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorAlreadyExistsException(String message) {
		super(message);
	}

}
