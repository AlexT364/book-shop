package shop.services.book;

import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;

public interface BookService {

	public BookDto addBook(CreateEditBookDto bookToCreate, byte[] coverImage);

	public BookDto updateBook(long bookId, CreateEditBookDto bookWithModifications, byte[] coverImage);
	
	public void deleteBook(long bookId);
}
