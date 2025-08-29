package shop.dto.discount;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscountDto {
	
	private Long id;
	private String name;
	private String description;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private boolean active;
	private boolean expired;
}
