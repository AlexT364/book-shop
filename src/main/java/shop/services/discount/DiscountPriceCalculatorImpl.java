package shop.services.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.persistence.entities.Discount;
import shop.persistence.entities.enums.DiscountType;

@Service
@RequiredArgsConstructor
public class DiscountPriceCalculatorImpl implements DiscountPriceCalculator{
	
	private static final BigDecimal MIN_PRICE = BigDecimal.valueOf(1.00);
	
	@Override
	public BigDecimal calculateBestPrice(Set<Discount> discounts, BigDecimal basePrice) {
		if(discounts == null || discounts.isEmpty()) {
			return basePrice;
		}
		return discounts.stream()
				.map(discount -> applyDiscount(basePrice, discount))
				.min(Comparator.naturalOrder())
				.orElse(basePrice);
	}
	
	/**
	 * Applies single discount to the given price.
	 * <p>
	 * Supports {@link DiscountType#PERCENT}(percentage off) and {@link DiscountType#FIXED}(fixed amount off).
	 * Result price will not fall below {@link #MIN_PRICE} and rounded to 2 decimals (HALF_UP).
	 * @param price - original price
	 * @param discount - discount to apply
	 * @return discounted price 
	 */
	private BigDecimal applyDiscount(BigDecimal price, Discount discount) {
		if(discount.getType() == DiscountType.PERCENT) { // DiscountType.PERCENT
			BigDecimal percent = discount.getAmount().divide(BigDecimal.valueOf(100));
			BigDecimal discountedPrice = price.multiply(BigDecimal.ONE.subtract(percent));
			return discountedPrice.max(MIN_PRICE).setScale(2, RoundingMode.HALF_UP);
		}else { // DiscountType.FIXED
	        BigDecimal discounted = price.subtract(discount.getAmount());
	        return discounted.max(MIN_PRICE).setScale(2, RoundingMode.HALF_UP);
	    }
	}


}
