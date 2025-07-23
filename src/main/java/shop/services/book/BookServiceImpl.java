package shop.services.book;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.exceptions.book.BookAlreadyExistsException;
import shop.exceptions.book.BookNotFoundException;
import shop.mapping.mappers.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.repositories.book.BookRepository;
import shop.services.book.helper.BookAssembler;
import shop.services.image.CoversImageService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final BookAssembler bookAssembler;
	private final CoversImageService coverImageService;
	private final BookMapper bookMapper;

	@Override
	@Transactional
	public BookDto addBook(CreateEditBookDto dto, byte[] coverImage) {
		if(bookRepository.existsByIsbn(dto.getIsbn())) {
			throw new BookAlreadyExistsException(dto.getIsbn());
		}
		
		Book entity = createBookAndSaveCover(dto, coverImage);

		return bookMapper.toBookDto(entity);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "books", key = "#result.id")
	public BookDto updateBook(long bookId, CreateEditBookDto dto, byte[] coverImage) {
		Book entity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
				.orElseThrow(() -> new BookNotFoundException(bookId));

		updateBookAndSaveCover(dto, entity, coverImage);
		
		return bookMapper.toBookDto(entity);
	}
	
	@Override
	@Transactional
	@Caching(evict = { @CacheEvict(cacheNames = "books", key = "#bookId"),
			@CacheEvict(cacheNames = "books", key = "#bookId + ':score'") })
	public void deleteBook(long bookId) {
		Book entity = bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException(bookId));
		deleteBookAndCover(entity);
	}
	
	
	private Book createBookAndSaveCover(CreateEditBookDto dto, byte[] coverImage) {
		Book entity = bookAssembler.createBookFromDto(dto);
		entity = bookRepository.save(entity);
		coverImageService.saveImage(entity.getIsbn(), coverImage);
		return entity;
	}
	
	private void updateBookAndSaveCover(CreateEditBookDto dto, Book entity, byte[] coverImage) {
		bookAssembler.updateBookFromDto(dto, entity);
		bookRepository.save(entity);
		coverImageService.saveImage(entity.getIsbn(), coverImage);
	}

	
	private void deleteBookAndCover(Book entity) {
		coverImageService.deleteImage(entity.getIsbn());
		bookRepository.delete(entity);
	}
}

