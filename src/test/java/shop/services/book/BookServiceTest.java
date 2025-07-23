package shop.services.book;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.exceptions.book.BookAlreadyExistsException;
import shop.exceptions.book.BookNotFoundException;
import shop.mapping.mappers.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.repositories.book.BookRepository;
import shop.services.book.helper.BookAssembler;
import shop.services.image.CoversImageService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	
	@InjectMocks
	private BookServiceImpl bookService;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private BookAssembler bookDtoEditor;
	@Mock
	private CoversImageService coversImageService;
	@Mock
	private BookMapper bookMapper;
	
	@Test
	public void addBook_ShouldThrowException_WhenBookAlreadyExists() {
		CreateEditBookDto dto = new CreateEditBookDto();
		dto.setIsbn("1234567890");
		byte[] coverImage = new byte[0];
		
		when(bookRepository.existsByIsbn(dto.getIsbn())).thenReturn(true);
		
		assertThrows(BookAlreadyExistsException.class, () -> bookService.addBook(dto, coverImage));
		
		verify(bookRepository).existsByIsbn(dto.getIsbn());
		verifyNoMoreInteractions(bookRepository);
		verifyNoInteractions(bookDtoEditor, coversImageService, bookMapper);
	}
	
	@Test
	public void addBook_HappyPath_WhenArgumentsAreValid() {
		//Given
		CreateEditBookDto dto = new CreateEditBookDto();
		dto.setIsbn("1234567890");
		byte[] coverImage = new byte[0];
		Book entity = new Book();
		BookDto expected = new BookDto();
		
		when(bookRepository.existsByIsbn(dto.getIsbn())).thenReturn(false);
		when(bookDtoEditor.createBookFromDto(dto)).thenReturn(entity);
		when(bookRepository.save(entity)).thenReturn(entity);
		when(bookMapper.toBookDto(entity)).thenReturn(expected);
		
		//When
		BookDto result = bookService.addBook(dto, coverImage);
		
		//Then
		assertNotNull(result);
		assertInstanceOf(BookDto.class, result);
		
		verify(bookRepository).existsByIsbn(dto.getIsbn());
		verify(bookRepository).save(entity);
		verify(coversImageService).saveImage(entity.getIsbn(), coverImage);
	}
	
	@Test
	public void updateBook_ShouldThrowException_WhenBookNotFound() {
		long bookId = 1L;
		CreateEditBookDto dto = new CreateEditBookDto();
		byte[] coverImage = new byte[0];
		
		when(bookRepository.findByIdWithAuthorsAndGenres(bookId)).thenReturn(Optional.empty());
		
		assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, dto, coverImage));
		
		verify(bookRepository).findByIdWithAuthorsAndGenres(bookId);
		verifyNoMoreInteractions(bookRepository);
		verifyNoInteractions(bookDtoEditor, coversImageService, bookMapper);
	}
	
	@Test
	public void updateBook_HappyPath_WhenBookExists() {
		long bookId = 1L;
		CreateEditBookDto dto = new CreateEditBookDto();
		dto.setIsbn("1234567890");
		byte[] coverImage = new byte[0];
		Book entity = new Book();
		BookDto expected = new BookDto();
		
		when(bookRepository.findByIdWithAuthorsAndGenres(bookId)).thenReturn(Optional.of(entity));
		when(bookRepository.save(entity)).thenReturn(entity);
		when(bookMapper.toBookDto(entity)).thenReturn(expected);
		BookDto result = bookService.updateBook(bookId, dto, coverImage);
		
		assertNotNull(result);
		assertInstanceOf(BookDto.class, result);
		
		verify(bookRepository).findByIdWithAuthorsAndGenres(bookId);
		verify(bookRepository).save(entity);
		verify(coversImageService).saveImage(entity.getIsbn(), coverImage);
	}
	
	@Test
	public void deleteBook_ShouldThrowException_WhenBookNotFound() {
		long bookId = 1L;
		
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
		
		assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
		
		verify(bookRepository).findById(bookId);
		verifyNoMoreInteractions(bookRepository);
		verifyNoInteractions(bookDtoEditor, coversImageService, bookMapper);
	}
	
	@Test
	public void deleteBook_ShouldDeleteBookAndCover_WhenBookExists() {
		long bookId = 1L;
		Book entity = new Book();
		
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(entity));
		
		bookService.deleteBook(bookId);
		
		verify(coversImageService).deleteImage(entity.getIsbn());
		verify(bookRepository).delete(entity);
	}
}


























