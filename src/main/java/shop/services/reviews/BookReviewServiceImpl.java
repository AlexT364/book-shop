package shop.services.reviews;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.reviews.BookReviewDto;
import shop.exceptions.book.BookNotFoundException;
import shop.exceptions.user.UserNotFoundException;
import shop.mapping.mappers.BookReviewMapper;
import shop.persistence.entities.Book;
import shop.persistence.entities.BookReview;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.BookReviewPK;
import shop.persistence.entities.projections.BookScoreProjection;
import shop.persistence.repositories.BookReviewRepository;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.book.BookRepository;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService{
	
	private final BookReviewRepository bookReviewRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final BookReviewMapper bookReviewMapper;
	
	@Override
	public Page<BookReviewDto> getBookReviews(long bookId, Pageable pageable) {
		Page<BookReviewDto> br = bookReviewRepository.findPageByBookId(bookId, pageable).map(bookReviewMapper::mapToDto);
		return br;
	}
	
	@Override
	@Transactional
	public Optional<BookReviewDto> getUsersReview(long bookId, String username) {
		Optional<BookReviewDto> reviewDto = bookReviewRepository.findByUsernameAndBookId(bookId, username).map(bookReviewMapper::mapToDto);
		
		return reviewDto;
	}
	
	@Override
	@Transactional
	public void deleteReview(long bookId, String authorUsername) {
		bookReviewRepository.deleteByUsernameAndBookId(bookId, authorUsername);
	}

	@Override
	@Transactional
	public void addNewReview(String username, long bookId, String review, int score) {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("User not found"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
		
		if(bookReviewRepository.existsById(new BookReviewPK(user.getId(), book.getId()))){
			throw new RuntimeException("Review already exists");
		}
		
		BookReview bookReview = new BookReview();
		bookReview.setUserBookAndCreateId(user, book);
		bookReview.setAddedAt(LocalDateTime.now());
		if(review.length() != 0) {
			bookReview.setReview(review);
		}
		bookReview.setScore(score);
		bookReviewRepository.save(bookReview);
	}
	
	public Map<Long, Double> findAvgScoresForBooks(List<Long> bookIds) {
		Map<Long, Double> booksScores = bookReviewRepository.findAvgRatingsByBooksIds(bookIds)
				.stream()
				.collect(Collectors.toMap(BookScoreProjection::getBookId, BookScoreProjection::getAvgScore));
		return booksScores;
	}

	@Override
	public Optional<Double> findAvgScoreForBook(Long bookId) {
		return Optional.ofNullable(bookReviewRepository.findAvgRatingByBookId(bookId));
	}
	
}





















