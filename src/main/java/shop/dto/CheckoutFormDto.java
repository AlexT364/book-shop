package shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CheckoutFormDto {
	
	private String shipAddress;
	private String shipCity;
	private String shipCountry;
	@NotBlank(message = "Phone number required.")
	@Size(min = 6, message = "Phone number min length 6 digits.")
	private String phone;
	@Override
	public String toString() {
		return "OrderFormDto [shipAddress=" + shipAddress + ", shipCity=" + shipCity
				+ ", shipCountry=" + shipCountry + ", phone=" + phone + "]";
	}
	
}
