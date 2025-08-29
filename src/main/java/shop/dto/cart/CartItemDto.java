package shop.dto.cart;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
	
	private Long id;

	private String title;
	
	private String isbn;
	
	private BigDecimal price;
	
	private BigDecimal priceWithDiscount;
	
	private int quantity;
	
	private BigDecimal subtotalPrice;
	
	
}

