package shop.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.persistence.entities.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDto {
	
	private Long id;
	private String username;
	private LocalDate orderDate;
	private int numOfItems;
	private String shipAddress;
	private String shipCity;
	private String shipCountry;
	@NotBlank(message = "Phone required.")
	private String phone;
	private BigDecimal totalPrice;
	@NotNull(message = "Status required. One of: NEW, CONFIRMED, SHIPPED, COMPLETE, CANCELLED.")
	private OrderStatus status;
}
