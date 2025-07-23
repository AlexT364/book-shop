package shop.persistence.entities.embeddables;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsPK {
	
	@Column(nullable = false)
	private Long orderId;
	@Column(nullable = false)
	private Long bookId;
	@Override
	public int hashCode() {
		return Objects.hash(bookId, orderId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OrderDetailsPK)) {
			return false;
		}
		OrderDetailsPK other = (OrderDetailsPK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(orderId, other.orderId);
	}
	
}
