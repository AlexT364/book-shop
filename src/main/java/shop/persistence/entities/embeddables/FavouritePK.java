package shop.persistence.entities.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouritePK implements Serializable{
	
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
		if (!(obj instanceof FavouritePK)) {
			return false;
		}
		FavouritePK other = (FavouritePK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(userId, other.userId);
	}
	
}
