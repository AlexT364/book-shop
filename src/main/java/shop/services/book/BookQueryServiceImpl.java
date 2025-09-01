package shop.services.book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.ShopRequestDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.dto.populators.BookDtoPopulator;
import shop.exceptions.book.BookNotFoundException;
import shop.mapping.mappers.book.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.repositories.book.BookRepository;
import shop.services.book.cache.BookCacheService;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService {

	private final BookRepository bookRepository;
	private final BookCacheService bookCacheService;
	private final BookMapper bookMapper;
	private final BookDtoPopulator bookDtoPopulator;

	@Override
	public BookDto getBookById(Long bookId) {
		return loadBookDto(bookId, null);
	}

	@Override
	public BookDto getBookById(Long bookId, String username) {
		return loadBookDto(bookId, username);
	}

	private BookDto loadBookDto(Long bookId, String username){
		BookDto bookDto = getBookFromCacheOrLoad(bookId);
		bookDto.setUnitsAvailable(bookRepository.findUnitAvailableById(bookDto.getId()));
		if(username != null){
			bookDtoPopulator.populateBookDto(bookDto, username);
		}else{
			bookDtoPopulator.populateBookDto(bookDto);
		}
		
		return bookDto;
	}

	@Override
	public CreateEditBookDto getBookByIdForEdit(Long bookId) {
		Book bookEntity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with id=%d not found".formatted(bookId)));

		return bookMapper.toCreateEditDto(bookEntity);
	}
	
	@Override
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto) {
		return loadBooksPageByFilter(shopRequestDto, null);
	}

	@Override
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto, String username) {
		return loadBooksPageByFilter(shopRequestDto, username);
	}

	private Page<ShortBookDto> loadBooksPageByFilter(ShopRequestDto shopRequestDto, String username) {
		Pageable pageable = formShopPageable(shopRequestDto);

		Page<Book> page = bookRepository.findCriteriaBooks(pageable, shopRequestDto, false);
		Page<ShortBookDto> dtoPage = page.map(bookMapper::toShortDto);

		if (username != null) {
			bookDtoPopulator.populateShortBookDtos(dtoPage.getContent(), username);
		} else {
			bookDtoPopulator.populateShortBookDtos((dtoPage.getContent()));
		}
		
		return dtoPage;
	}
	
	@Override
	public BigDecimal findMaxPriceByFilters(ShopRequestDto request) {
		return bookRepository.findMaxPriceWithFilters(request);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookDto> getAuthorsHighestRatedBook(Long authorId) {
		 return bookRepository.findAuthorsHighestRatedBook(authorId).map(book -> {
			BookDto dto = bookMapper.toBookDto(book);
			bookDtoPopulator.populateBookDto(dto);
			return dto;
		});
	}

	@Override
	public List<ShortBookDto> getLatestBooks() {
		List<ShortBookDto> latestBookDtos = bookRepository.findLatestBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoPopulator.populateShortBookDtos(latestBookDtos);

		return latestBookDtos;
	}
	
	@Override
	public List<ShortBookDto> getPopularBooks() {
		List<ShortBookDto> popularBooksDtos = bookRepository.findPopularBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoPopulator.populateShortBookDtos(popularBooksDtos);

		return popularBooksDtos;
	}
	
	@Override
	public List<ShortBookDto> getHighestRatedBooks() {
		List<ShortBookDto> highestRatedBooksDtos = bookRepository.findHighestRatedBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoPopulator.populateShortBookDtos(highestRatedBooksDtos);
		
		return highestRatedBooksDtos;
	}
	
	private Pageable formShopPageable(ShopRequestDto shopRequestDto) {
		Order order = switch (shopRequestDto.getSort()) {
		case PRICE_ASC -> Order.asc("price");
		case PRICE_DESC -> Order.desc("price");
		case TITLE_ASC -> Order.asc("title");
		case TITLE_DESC -> Order.desc("title");
		default -> new Order(Direction.DESC, "id");
		};

		return PageRequest.of(shopRequestDto.getPageNumber(), shopRequestDto.getPageSize(), Sort.by(order));
	}
	
	private BookDto getBookFromCacheOrLoad(long bookId) {
		return bookCacheService.getCachedBook(bookId)
				.orElseGet(() -> {
					Book bookEntity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
							.orElseThrow(() -> new BookNotFoundException("Book not found."));
					BookDto dto = bookMapper.toBookDto(bookEntity);
					bookCacheService.cacheBook(dto);
					return dto;
				});
	}
}
	
