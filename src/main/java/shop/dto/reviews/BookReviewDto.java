package shop.dto.reviews;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookReviewDto {
	
	private String username;
	private LocalDateTime addedAt;
	private String review;
	private int score;
}
