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
import lombok.ToString;
import shop.persistence.entities.embeddables.CartItemPK;

@Entity
@Table(name="cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartItem {
	
	@EmbeddedId
	private CartItemPK id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	private int quantity;
	private LocalDateTime addedAt;
	private boolean expired;
	
	
}
