package shop.exceptions.cart;

public class CartItemNotFoundException extends RuntimeException{

	public CartItemNotFoundException() {
		super();
	}

	public CartItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CartItemNotFoundException(String message) {
		super(message);
	}

}
