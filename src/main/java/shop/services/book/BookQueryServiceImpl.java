package shop.services.book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import shop.exceptions.book.BookNotFoundException;
import shop.mapping.mappers.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.repositories.book.BookRepository;
import shop.services.book.cache.BookCacheService;
import shop.services.favourite.FavouriteService;
import shop.services.reviews.BookReviewService;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService {

	private final BookRepository bookRepository;
	private final BookReviewService bookReviewService;
	private final FavouriteService favouriteService;
	private final BookCacheService bookCacheService;
	private final BookMapper bookMapper;

	@Override
	public BookDto getBookById(long bookId) {
		return this.getBookById(bookId, null);
	}

	@Override
	public BookDto getBookById(long bookId, String username) {
		BookDto bookDto = bookCacheService.getCachedBook(bookId).orElse(null);

		// Cache miss
		if (bookDto == null) {
			Book bookEntity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
					.orElseThrow(() -> new BookNotFoundException("Book not found."));
			bookDto = bookMapper.toBookDto(bookEntity);
			bookCacheService.cacheBook(bookDto);
		}

		bookDto.setUnitsAvailable(bookRepository.findUnitAvailableById(bookDto.getId()));

		Double score = bookCacheService.getCachedScore(bookId).orElse(null);
		if (score == null) {
			score = bookReviewService.findAvgScoreForBook(bookId).orElse(0.0);
			if (score != 0.0) {
				bookCacheService.cacheScore(bookId, score);
			}
		}

		bookDto.setScore(score);

		boolean isFavourite = username == null ? false
				: favouriteService.checkIfBookInUsersFavourites(username, bookId);
		bookDto.setFavourite(isFavourite);
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
		List<Long> bookIds = page.map(Book::getId).toList();

		Map<Long, Double> booksScores = bookReviewService.findAvgScoresForBooks(bookIds);
		Set<Long> favouriteIds = username == null ? Set.of()
				: favouriteService.findFavouriteBookIdsForUser(username, bookIds);

		Page<ShortBookDto> dtoPage = page.map(book -> {
			ShortBookDto dto = bookMapper.toShortDto(book);
			dto.setScore(booksScores.getOrDefault(book.getId(), 0.0));
			dto.setFavourite(favouriteIds.contains(book.getId()));
			return dto;
		});

		return dtoPage;
	}

	@Override
	public BigDecimal findMaxPriceByFilters(ShopRequestDto request) {
		BigDecimal result = bookRepository.findMaxPriceWithFilters(request);
		return result;
	}

	@Override
	@Transactional
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
		Pageable pageable = PageRequest.of(0, 3);
		List<ShortBookDto> latestBooks = bookRepository.findLatestBooks(pageable)
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		return latestBooks;
	};

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

	@Override
	public List<ShortBookDto> getPopularBooks() {
		Pageable pageable = PageRequest.of(0, 3);
		List<ShortBookDto> popularBooks = bookRepository.findPopularBooks(pageable)
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		return popularBooks;
	}

	@Override
	public List<ShortBookDto> getHighestRatedBooks() {
		Pageable pageable = PageRequest.of(0, 3);
		List<ShortBookDto> highestRatedBooks = bookRepository.findHighestRatedBooks(pageable)
				.stream()
				.map(bookMapper::toShortDto)
				.toList();
		return highestRatedBooks;
	}
	
	
}
	
