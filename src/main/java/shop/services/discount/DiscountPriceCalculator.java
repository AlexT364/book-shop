package shop.services.discount;

import java.math.BigDecimal;
import java.util.Set;

import shop.persistence.entities.Discount;

public interface DiscountPriceCalculator {
	
	/**
	 * Calculates the lowest possible price for a product based on the given set of discounts.
	 * <p>
	 * Rules:
	 * <ul>
	 * 		<li>If {@code discounts} is {@code null} or empty, the {@code basePrice} is returned.</li>
	 * 		<li>If multiple discounts are applicable, the one that yields the lowest price is chosen.</li>
	 * 		<li>The price is never reduced below {@code 1.00} (defined by {@link #MIN_PRICE}).</li>
	 * 		<li>The final result is rounded to 2 decimal places using {@link RoundingMode#HALF_UP}.</li>
	 * </ul>
	 * @param discounts - the set of discounts to apply (may be {@code null} or empty)
	 * @param basePrice - the original price before applying discounts
	 * @return the lowest price after applying discounts
	 */
	public BigDecimal calculateBestPrice(Set<Discount> discounts, BigDecimal basePrice);
}
