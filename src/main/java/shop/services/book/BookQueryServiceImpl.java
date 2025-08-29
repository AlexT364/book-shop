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
import shop.mapping.mappers.book.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.entities.Discount;
import shop.persistence.repositories.book.BookRepository;
import shop.services.book.cache.BookCacheService;
import shop.services.discount.DiscountPriceCalculator;
import shop.services.discount.DiscountQueryService;
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
	private final DiscountQueryService discountQueryService;
	private final DiscountPriceCalculator discountPriceCalculator;

	@Override
	public BookDto getBookById(long bookId) {
		return this.getBookById(bookId, null);
	}

	@Override
	public BookDto getBookById(long bookId, String username) {
		BookDto bookDto = bookCacheService.getCachedBook(bookId).orElse(null);

		// Cache miss for BookDto
		if (bookDto == null) {
			Book bookEntity = bookRepository.findByIdWithAuthorsAndGenres(bookId)
					.orElseThrow(() -> new BookNotFoundException("Book not found."));
			bookDto = bookMapper.toBookDto(bookEntity);
			bookCacheService.cacheBook(bookDto);
		}

		bookDto.setUnitsAvailable(bookRepository.findUnitAvailableById(bookDto.getId()));

		Double score = bookCacheService.getCachedScore(bookId).orElse(null);
		//Cache miss for BookDto.score
		if (score == null) {
			score = bookReviewService.findAvgScoreForBook(bookId).orElse(0.0);
			if (score != 0.0) {
				bookCacheService.cacheScore(bookId, score);
			}
		}
		bookDto.setScore(score);
		
		//Setting BookDto.favourite field
		boolean isFavourite = username == null ? false
				: favouriteService.checkIfBookInUsersFavourites(username, bookId);
		bookDto.setFavourite(isFavourite);
		
		//Setting BookDto.priceWithDiscount
		Set<Discount> discounts = discountQueryService.findDiscountsForBook(bookDto.getId());
		BigDecimal priceWithDiscount = discountPriceCalculator.calculateBestPrice(discounts, bookDto.getPrice());
		if(priceWithDiscount.compareTo(bookDto.getPrice()) < 0) {
			bookDto.setPriceWithDiscount(priceWithDiscount);
		}
		
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

		Map<Long, Set<Discount>> discountsByBookId = discountQueryService.findDiscountsForBooks(bookIds);
		
		//Setting ShortBookDto.score and ShortBookDto.favourite
		Map<Long, Double> booksScores = bookReviewService.findAvgScoresForBooks(bookIds);
		Set<Long> favouriteIds = username == null ? Set.of()
				: favouriteService.findFavouriteBookIdsForUser(username, bookIds);

		Page<ShortBookDto> dtoPage = page.map(book -> {
			ShortBookDto dto = bookMapper.toShortDto(book);
			dto.setScore(booksScores.getOrDefault(book.getId(), 0.0));
			dto.setFavourite(favouriteIds.contains(book.getId()));
			
			BigDecimal priceWithDiscount = discountPriceCalculator.calculateBestPrice(discountsByBookId.get(book.getId()), book.getPrice());
			dto.setPriceWithDiscount(priceWithDiscount.compareTo(book.getPrice()) < 0 ? priceWithDiscount : null);
			return dto;
		});
		
		return dtoPage;
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
		List<Book> latestBooksEntities = bookRepository.findLatestBooks();
		
		return this.mapBooksToShortDtosWithDiscounts(latestBooksEntities);
	};

	@Override
	public List<ShortBookDto> getPopularBooks() {
		List<Book> popularBooksEntities = bookRepository.findLatestBooks();
		
		return this.mapBooksToShortDtosWithDiscounts(popularBooksEntities);
	}

	@Override
	public List<ShortBookDto> getHighestRatedBooks() {
		List<Book> highestRatedBooksEntities = bookRepository.findHighestRatedBooks();
		
		return this.mapBooksToShortDtosWithDiscounts(highestRatedBooksEntities);
	}
	
	private List<ShortBookDto> mapBooksToShortDtosWithDiscounts(List<Book> bookList){
		List<Long> bookIds = bookList.stream().map(Book::getId).toList();
		Map<Long, Set<Discount>> discountsByBookId = discountQueryService.findDiscountsForBooks(bookIds);
		
		List<ShortBookDto> dtoList = bookList.stream().map(book -> {
			ShortBookDto dto = bookMapper.toShortDto(book);
			BigDecimal priceWithDiscount = discountPriceCalculator
					.calculateBestPrice(discountsByBookId.get(book.getId()), book.getPrice());
			dto.setPriceWithDiscount(priceWithDiscount.compareTo(book.getPrice()) < 0 ? priceWithDiscount : null);
			return dto;
		}).toList();
		return dtoList;
	}
	
}
	
