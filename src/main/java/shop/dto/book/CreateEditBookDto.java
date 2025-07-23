package shop.dto.book;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEditBookDto {
	
	private long id;
	
	@NotBlank(message = "Title required.")
	@Size(min = 2, message = "Title length minimum 2 letters.")
	private String title;
	
	private String description;
	
	@NotBlank(message = "ISBN required.")
	@Size(min = 10, max = 10, message="ISBN must be of length 10.")
	private String isbn;
	
	@NotNull(message = "Price required.")
	@Min(value = 1, message = "Minimum price is 1.")
	private BigDecimal price;
	
	@NotNull(message = "Units in stock required.")
	@Min(value = 0, message="Minimum quantity is 0.")
	private int unitsInStock;
	
	private Set<Long> authors = new HashSet<>();
	
	private Set<Integer> genres = new HashSet<>();
	
	@Override
	public String toString() {
		return "CreateBookDto [title=" + title + ", description=" + description + ", authors=" + authors + ", isbn="
				+ isbn + ", price=" + price + ", unitsInStock=" + unitsInStock + ", genres=" + genres + "]";
	}
	
}
