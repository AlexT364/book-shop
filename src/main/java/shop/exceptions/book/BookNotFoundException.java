package shop.exceptions.book;

public class BookNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BookNotFoundException(String message) {
		super(message);
	}
	
	public BookNotFoundException(long bookId) {
		super("Book with id=%d not found".formatted(bookId));
	}

	public BookNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookNotFoundException() {
		super();
	}
	
	
}
