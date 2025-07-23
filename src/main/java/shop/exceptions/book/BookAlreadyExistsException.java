package shop.exceptions.book;

public class BookAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BookAlreadyExistsException(String isbn) {
		super("Book with isbn=%s already exists".formatted(isbn));
	}
	
}
