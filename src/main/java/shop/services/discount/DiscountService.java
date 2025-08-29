package shop.services.discount;

import shop.dto.discount.CreateDiscountDto;

public interface DiscountService {
	
	/**
	 * Creates a discount based on the parameters provided in the DTO
	 * 
	 * The discount must be associated either with specific books or with genres,
     * but not both. If the request is invalid, an exception will be thrown.
	 * 
	 * @param newDiscountRequest - DTO containing discount details
	 * @throws IllegalArgumentException if validation fails(invalid dates,
	 * percentage greater than or equal to 100, or invalid association)
	 */
	public void create(CreateDiscountDto newDiscountRequest);
	/**
	 * Deletes the discount with given identifier
	 * @param discountId - discount identifier to delete
	 */
	public void delete(Long discountId);
	/**
	 * Activates discount by setting its {@code active} flag to {@code true}
	 * @param discountId - discount identifier to activate
	 * @throws DiscountNotFoundException if discount with given identifier was not found
	 */
	public void activateDiscount(Long discountId);
	/**
	 * Deactivates discount by setting its {@code active} flag to {@code false}
	 * @param discountId - discount identifier to deactivate
	 * @throws DiscountNotFoundException if discount with given identifier was not found
	 */
	public void deactivateDiscount(Long discountId);
}
