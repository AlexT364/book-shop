package shop.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.embeddables.BookReviewPK;

@Entity
@Table(name = "book_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookReview {
	
	@EmbeddedId
	private BookReviewPK id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("bookId")
	@JoinColumn(name="book_id", nullable = false)
	private Book book;
	
	public void setUserBookAndCreateId(User user, Book book) {
		this.user = user;
		this.book = book;
		this.id = new BookReviewPK(user.getId(), book.getId());
	}
	
	private String review;
	private LocalDateTime addedAt;
	private int score;

	
	
}
