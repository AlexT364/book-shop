package shop.dto.book;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortBookDto {
	
	private Long id;
	
	private String title;
	
	private String isbn;
	
	private BigDecimal price;
	
	private int unitsAvailable;
	
	private Double score;
	
	private boolean favourite;
	
}
