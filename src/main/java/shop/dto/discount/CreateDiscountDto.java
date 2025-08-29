package shop.dto.discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import shop.persistence.entities.enums.DiscountType;

@Data
@ToString
public class CreateDiscountDto {
		
		@NotBlank(message = "Name of sale required")
		@Size(min=3, max=255, message = "Name of sale cannot be less than 3 characters and more than 255 characters in length")
	    private String name;
	    private String description;
	    @NotNull
	    @Min(value = 1)
	    private BigDecimal amount;
	    @NotNull
	    private DiscountType type;
	    @NotNull(message="Start date required")
	    private LocalDateTime startDate;
	    @NotNull(message="End date required")
	    private LocalDateTime endDate;
	    private boolean active;
	    private List<Long> bookIds = new ArrayList<>();
	    private List<Long> genreIds = new ArrayList<>();

}
