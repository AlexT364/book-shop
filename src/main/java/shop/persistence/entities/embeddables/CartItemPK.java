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
public class CartItemPK implements Serializable{

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
		if (!(obj instanceof CartItemPK)) {
			return false;
		}
		CartItemPK other = (CartItemPK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(userId, other.userId);
	}
	
	
}
