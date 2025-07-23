package shop.exceptions.author;

public class AuthorNotFoundException extends RuntimeException {

	public AuthorNotFoundException() {
	}

	public AuthorNotFoundException(String message) {
		super(message);
	}


	public AuthorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
