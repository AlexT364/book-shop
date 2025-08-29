package shop.exceptions.discount;

public class DiscountNotFoundException extends RuntimeException{

	public DiscountNotFoundException() {
		super();
	}
	
	public DiscountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiscountNotFoundException(String message) {
		super(message);
	}

}
