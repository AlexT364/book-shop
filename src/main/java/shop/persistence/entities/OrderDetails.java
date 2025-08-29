package shop.persistence.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
import shop.persistence.entities.embeddables.OrderDetailsPK;

@Entity
@Table(name="order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

	@EmbeddedId
	private OrderDetailsPK pk;
	
	@ManyToOne
	@MapsId("orderId")
	@JoinColumn(name="order_id")
	private Order order;
	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name="book_id")
	private Book book;
	
	private int quantity;
	private BigDecimal unitPrice;
	@Column(name="discount_unit")
	private BigDecimal discountPerUnit;
	@Column(name = "final_unit")
	private BigDecimal finalUnitPrice;
	private BigDecimal subtotal;
}
