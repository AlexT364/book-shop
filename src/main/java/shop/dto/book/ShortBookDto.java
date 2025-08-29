package shop.dto.book;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortBookDto {
	
	private Long id;
	
	private String title;
	
	private String isbn;
	
	private BigDecimal price;
	
	private BigDecimal priceWithDiscount;
	
	private int unitsAvailable;
	
	private Double score;
	
	private boolean favourite;
	
}
