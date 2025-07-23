package shop.exceptions.security;

public class TokenExpiredException extends RuntimeException{

	public TokenExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenExpiredException(String message) {
		super(message);
	}

}
