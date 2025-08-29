package shop.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.persistence.entities.enums.DiscountType;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@ToString
public class Discount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "discount_id")
	private Long id;
	private String name;
	private String description;
	private BigDecimal amount;
	@Column(name="discount_type")
	@Enumerated(EnumType.STRING)
	private DiscountType type;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private boolean active;
	
	@ManyToMany
	@JoinTable(name = "discount_books", joinColumns = @JoinColumn(name = "discount_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	List<Book> books;
	@ManyToMany
	@JoinTable(name = "discount_genres", joinColumns = @JoinColumn(name = "discount_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	List<Genre> genres;
}
