package shop.services.reviews;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.reviews.BookReviewDto;

public interface BookReviewService {
	
	void addNewReview(String username, long bookId, String review, int score);
	
	Page<BookReviewDto> getBookReviews(long bookId, Pageable pageable);

	Optional<BookReviewDto> getUsersReview(long bookId, String username);
	
	void deleteReview(long bookId, String authorUsername);
	
	Map<Long, Double> findAvgScoresForBooks(List<Long> bookIds);

	Optional<Double> findAvgScoreForBook(Long bookId);
}
