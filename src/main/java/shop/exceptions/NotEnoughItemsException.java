package shop.exceptions;

public class NotEnoughItemsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NotEnoughItemsException(String message) {
		super(message);
	}

}
