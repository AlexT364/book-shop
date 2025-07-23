package shop.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.BookReview;
import shop.persistence.entities.embeddables.BookReviewPK;
import shop.persistence.entities.projections.BookScoreProjection;

public interface BookReviewRepository extends JpaRepository<BookReview, BookReviewPK>{
	
		/**
		 * Finds page of book reviews, not including reviews without text.
		 * @param bookId
		 * @param pageable
		 * @return page of BookReview.
		 */
		@Query("""
				SELECT br FROM BookReview br 
				JOIN FETCH br.user u
				WHERE br.book.id = :bookId AND br.review IS NOT NULL
				""")
		Page<BookReview> findPageByBookId(long bookId, Pageable pageable);

		/**
		 * Finds users's book review for specific book.
		 * @param bookId
		 * @param username
		 * @return Optional of BookReview
		 */
		@Query("""
				SELECT br FROM BookReview br
				JOIN FETCH br.user u
				WHERE br.user.username = :username AND br.book.id = :bookId 
				""")
		Optional<BookReview> findByUsernameAndBookId(long bookId, String username);
		
		
		@Modifying
		@Query("""
				DELETE FROM BookReview br
				WHERE br.book.id = :bookId AND br.user.username = :username
				""")
		void deleteByUsernameAndBookId(long bookId, String username);
		
		@Query("""
				SELECT AVG(br.score) FROM BookReview br WHERE br.book.id = :bookId
				""")
		Double findAvgRatingByBookId(long bookId);
		
		@Query("""
				SELECT br.book.id AS bookId, AVG(br.score) AS avgScore
				FROM BookReview br WHERE br.book.id IN :bookIds
				GROUP BY br.book.id
				""")
		List<BookScoreProjection> findAvgRatingsByBooksIds(List<Long> bookIds);
}





