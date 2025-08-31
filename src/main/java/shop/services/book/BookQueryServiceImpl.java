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
import shop.services.reviews.BookReviewService;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService {

	private final BookRepository bookRepository;
	private final BookReviewService bookReviewService;
	private final BookCacheService bookCacheService;
	private final BookMapper bookMapper;
	private final BookDtoPopulator bookDtoEnricher;

	@Override
	public BookDto getBookById(long bookId) {
		return this.getBookById(bookId, null);
	}

	@Override
	public BookDto getBookById(long bookId, String username) {
		BookDto bookDto = this.getOrLoadBook(bookId);
		
		bookDto.setUnitsAvailable(bookRepository.findUnitAvailableById(bookDto.getId()));
		bookDtoEnricher.populateBookDto(bookDto, username);
		
		return bookDto;
	}

	@Override
	public CreateEditBookDto getBookByIdForEdit(long bookId) {
		Book bookEntity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with id=%d not found".formatted(bookId)));
		CreateEditBookDto bookDto = bookMapper.toCreateEditDto(bookEntity);

		return bookDto;
	}
	
	@Override
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto) {
		return this.getBooksPageByFilter(shopRequestDto, null);
	}

	@Override
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto, String username) {
		Pageable pageable = formShopPageable(shopRequestDto);

		Page<Book> page = bookRepository.findCriteriaBooks(pageable, shopRequestDto, false);
		Page<ShortBookDto> dtoPage = page.map(bookMapper::toShortDto);

		bookDtoEnricher.populateShortBookDtos(dtoPage.getContent(), username);
		
		return dtoPage;
	}
	
	@Override
	public BigDecimal findMaxPriceByFilters(ShopRequestDto request) {
		BigDecimal result = bookRepository.findMaxPriceWithFilters(request);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookDto> getAuthorsHighestRatedBook(Long authorId) {
		return bookRepository.findAuthorsHighestRatedBook(authorId).map(highestRatedBook -> {
			Double avgScore = bookReviewService.findAvgScoreForBook(highestRatedBook.getId()).get();
			BookDto dto = bookMapper.toBookDto(highestRatedBook);
			dto.setScore(avgScore);
			return dto;
		});
	}

	@Override
	public List<ShortBookDto> getLatestBooks() {
		List<ShortBookDto> latestBookDtos = bookRepository.findLatestBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoEnricher.populateShortBookDtos(latestBookDtos);
		return latestBookDtos;
	}
	
	@Override
	public List<ShortBookDto> getPopularBooks() {
		List<ShortBookDto> popularBooksDtos = bookRepository.findPopularBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoEnricher.populateShortBookDtos(popularBooksDtos);
		return popularBooksDtos;
	}
	
	@Override
	public List<ShortBookDto> getHighestRatedBooks() {
		List<ShortBookDto> highestRatedBooksDtos = bookRepository.findHighestRatedBooks()
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		
		bookDtoEnricher.populateShortBookDtos(highestRatedBooksDtos);
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
	
	private BookDto getOrLoadBook(long bookId) {
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
	
