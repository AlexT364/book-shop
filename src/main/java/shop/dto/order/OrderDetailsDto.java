package shop.dto.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.Book;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
	
	private Long id;
	private String title;
	private String isbn;
	
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotalPrice;
}
