package shop.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.embeddables.FavouritePK;

@Entity
@Table(name="favourite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {
	
	@EmbeddedId
	private FavouritePK id;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name="book_id", nullable=false)
	private Book book;
	private LocalDateTime addedAt;
}
