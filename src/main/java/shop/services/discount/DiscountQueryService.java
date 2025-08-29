package shop.services.discount;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;

import shop.dto.discount.DiscountDto;
import shop.persistence.entities.Discount;

public interface DiscountQueryService {

	/**
	 * Finds discounts for the given list of books;
	 * 
	 * @param bookIds - identifiers of books to check for applicable discounts
	 * @return a map where the key is the book identifier and the values is a set of applicable discounts;
	 * if a book has no discounts, the values is empty set
	 */
	public Map<Long, Set<Discount>> findDiscountsForBooks(List<Long> bookIds);
	
	/**
	 * Finds discounts available for specified bookId.
	 * 
	 * @param bookId - identifier of the book
	 * @return a set of discounts applicable for the given book;
	 * empty if none discounts are found
	 */
	public Set<Discount> findDiscountsForBook(Long bookId);
	
	/**
	 * Retrieves a page of discounts that are currently active({@code Discount.active=true});
	 * and which start date is before current time and its end date is after current time. 
	 * 
	 * @param pageNumber - zero-based index of the page to retrieve
	 * @return a page of discount DTOs representing active discounts
	 */
	public Page<DiscountDto> findCurrentActiveDiscounts(int pageNumber);
	
	/**
	 * Returns page of all discounts(including {@code Discount.active=false} and expired ones) for administrative purposes
	 * 
	 * @param pageNumber - zero-based index of the page to retrieve
	 * @return a page of discount DTOs with an additional field indication whether each discount is expired
	 */
	public Page<DiscountDto> findDiscountsForAdmin(int pageNumber);
}
