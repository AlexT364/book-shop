package shop.exceptions.review;

public class BookReviewNotFoundException extends RuntimeException {

	public BookReviewNotFoundException() {
	}

	public BookReviewNotFoundException(String message) {
		super(message);
	}


	public BookReviewNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
