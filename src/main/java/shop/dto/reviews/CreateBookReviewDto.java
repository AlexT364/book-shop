package shop.dto.reviews;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookReviewDto {
	
	private String review;
	@Min(value = 1, message = "Score should be greater than 0")
	@Max(value = 5, message = "Score should be less than or equal 5")
	private int score;
}
