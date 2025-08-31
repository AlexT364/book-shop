package shop.services.book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import shop.dto.ShopRequestDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.exceptions.book.BookNotFoundException;
import shop.persistence.repositories.book.BookRepository;

public interface BookQueryService {
	/**
	 * Returns {@link BookDto} without user-specific data (e.g. favourite status).
	 *
	 * This method is a convenience wrapper for
	 * {@link #getBookById(long, String)} with {@code null} as
	 * the username.
	 * 
	 * @param bookId the unique identifier of the book
	 * @return a {@link BookDto} with general book information
	 * @throws BookNotFoundException if book with specified id was not found
	 */
	public BookDto getBookById(long bookId);

	/**
	 * Returns {@link BookDto} object with all information about specific book
	 * 
	 * @param bookId   the unique identifier of the book
	 * @param username string used to determine if book is marked as favourite by
	 *                 the user; if {@code null}, the {@code favourite} field in
	 *                 {@link BookDto} will be set to {@code false}
	 * 
	 * @return a {@link BookDto} containing all information about the specified book
	 * @throws BookNotFoundException if book with specified id was not found
	 * @implNote it is recommended to call {@link #getBookById(long)} method if no
	 *           username is available, instead of passing {@code null} string
	 *           explicitly.
	 */
	public BookDto getBookById(long bookId, String username);
	
	/**
	 * Returns a paginated list of books filtered to the specified criteria.
	 * 
	 * @param shopRequestDto a DTO containing pagination and filter criteria
	 * @param username       the username used to determine if book is marked as
	 *                       favourite; if {@code null}, the {@code favourite} field
	 *                       in {@link ShortBookDto} objects will be set to
	 *                       {@code false}
	 * 
	 * @return a {@link Page} of {@link ShortBookDto} matching specified filter
	 *         criteria with the {@code favourite} field populated based on the
	 *         user's preferences
	 * 
	 * @implNote It is recommended to call
	 *           {@link #getBooksPageByFilter(ShopRequestDto)} if no username is
	 *           available, instead of explicitly passing {@code null}.
	 */
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto, String username);

	/**
	 * Returns a paginated list of books that match the specified filtering
	 * criteria, assuming no user is authenticated (i.e., favourites are marked as
	 * {@code false}.
	 *
	 * This method is a convenience wrapper for
	 * {@link #getBooksPageByFilter(ShopRequestDto, String)} with {@code null} as
	 * the username.
	 *
	 * @param shopRequestDto a DTO containing pagination settings and filter
	 *                       criteria
	 * @return a {@link Page} of {@link ShortBookDto} objects matching the filter
	 *         criteria, with the {@code favourite} field always set to
	 *         {@code false}
	 */
	public Page<ShortBookDto> getBooksPageByFilter(ShopRequestDto shopRequestDto);

	public Optional<BookDto> getAuthorsHighestRatedBook(Long authorId);

	CreateEditBookDto getBookByIdForEdit(long bookId);
	
	BigDecimal findMaxPriceByFilters(ShopRequestDto shopRequestDto);

	/**
	 * Returns list that contains 3 latest added books and maps them to {@link ShortBookDto}.
	 * <p>
	 * Each resulting DTO may contain a discounted price, if applicable.
	 * 
	 * @return a non-null, possibly empty list of {@link ShortBookDto}
	 */
	public List<ShortBookDto> getLatestBooks();
	
	/**
	 * Returns list that contains 3 most popular books.
	 * <p>
	 * Popularity criteria are defined in {@link BookRepository#findPopularBooks()}.
	 * Each resulting DTO may contain a discounted price, if applicable.
	 * @return a non-null, possibly empty list of {@link ShortBookDto}
	 */
	public List<ShortBookDto> getPopularBooks();
	
	/**
	 * Returns list that contains 3 highest-rated books and maps them to {@link ShortBookDto}.
	 * <p>
	 * Rating criteria are defined in {@link BookRepository#findHighestRatedBooks()}.
	 * @return a non-null, possibly empty list of {@link ShortBookDto}
	 */
	public List<ShortBookDto> getHighestRatedBooks();
}



