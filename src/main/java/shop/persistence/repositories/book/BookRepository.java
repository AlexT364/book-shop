package shop.persistence.repositories.book;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import shop.dto.book.BookDto;
import shop.persistence.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository{

	@Override
	@Query("""
			SELECT b FROM Book b WHERE b.id = :id
			""")
	public Optional<Book> findById(Long id);
	
	@Query("""
			SELECT b FROM Book b JOIN b.genres g WHERE g.name IN (:genre) AND b.price <= :price
			""")
	public Page<Book> findAllByGenre(Pageable pageable, List<String> genre, int price);

	@Query("""
			SELECT b FROM Book b WHERE b.price <= :price
			""")
	public Page<Book> findAllByPrice(Pageable byAvailability, int price);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT b FROM Book b WHERE b.id IN :ids
			""")
	public List<Book> findAllByIds(@Param("ids") Iterable<Long> ids);
	
	public boolean existsByIsbn(String isbn);
	
	@Query(value = """
		    SELECT b.*
		    FROM books b
		    JOIN(
		        SELECT br.book_id, AVG(br.score) AS avg_score
		        FROM book_reviews br
		        JOIN books_authors ba ON br.book_id = ba.book_id
		        WHERE ba.author_id = :authorId
		        GROUP BY br.book_id
		        ORDER BY avg_score DESC
		        LIMIT 1
		    ) AS avg_scores ON b.book_id = avg_scores.book_id
		""", nativeQuery = true)
	public Optional<Book> findAuthorsHighestRatedBook(Long authorId);

	@Query("""
			SELECT b.unitsInStock - b.unitsReserved FROM Book b WHERE id = :id
			""")
	public int findUnitAvailableById(Long id);

	@Query("""
			SELECT b 
			FROM Book b 
			ORDER BY b.addedAt DESC
			LIMIT 3
			""")
	public List<Book> findLatestBooks();

	@Query("""
			SELECT b FROM OrderDetails od 
			JOIN od.book b
			GROUP BY b
			ORDER BY SUM(od.quantity) DESC
			LIMIT 3
			""")
	public List<Book> findPopularBooks();
	
	@Query("""
			SELECT b FROM BookReview br 
			JOIN br.book b
			GROUP BY b
			ORDER BY AVG(br.score) DESC
			LIMIT 3
			""")
	public List<Book> findHighestRatedBooks();

	public List<Book> findTop10ByTitleContainingIgnoreCase(String query);

}

















