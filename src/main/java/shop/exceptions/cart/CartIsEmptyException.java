package shop.exceptions.cart;

public class CartIsEmptyException extends RuntimeException {

	public CartIsEmptyException() {
		super();
	}

	public CartIsEmptyException(String message) {
		super(message);
	}

}
