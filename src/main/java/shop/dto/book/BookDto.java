package shop.dto.book;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dto.author.AuthorDto;
import shop.dto.genre.GenreDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
	
	private Long id;
	
	private String title;
	
	private String description;
	
	private String isbn;
	
	private BigDecimal price;
	
	private BigDecimal priceWithDiscount;
	
	private Integer unitsAvailable;
	
	private List<AuthorDto> authors;
	
	private List<GenreDto> genres;
	
	private Double score;
	
	private boolean favourite;

	@Override
	public String toString() {
		return "BookDto [id=" + id + ", title=" + title + ", isbn=" + isbn + ", price="
				+ price + ", unitsAvailable=" + unitsAvailable + ", score=" + score + "]";
	}
	

	
	
}
