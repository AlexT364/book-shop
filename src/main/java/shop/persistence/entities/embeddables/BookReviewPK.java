package shop.persistence.entities.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookReviewPK implements Serializable{
	
	private Long userId;
	private Long bookId;
	
	@Override
	public int hashCode() {
		return Objects.hash(bookId, userId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BookReviewPK)) {
			return false;
		}
		BookReviewPK other = (BookReviewPK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(userId, other.userId);
	}
}
