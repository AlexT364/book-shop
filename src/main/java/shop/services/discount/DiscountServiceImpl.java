package shop.services.discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.discount.CreateDiscountDto;
import shop.exceptions.discount.DiscountNotFoundException;
import shop.persistence.entities.Discount;
import shop.persistence.entities.enums.DiscountType;
import shop.persistence.repositories.DiscountRepository;
import shop.persistence.repositories.GenresRepository;
import shop.persistence.repositories.book.BookRepository;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{
		
	private final DiscountRepository discountRepository;
	private final BookRepository bookRepository;
	private final GenresRepository genresRepository;
	
	@Override
	@Transactional
	public void create(CreateDiscountDto newDiscountRequest) {
		
		this.validateDiscountCreateRequest(newDiscountRequest);
		Discount newDiscount = this.assembleDiscount(newDiscountRequest);
		
		newDiscount.setBooks(bookRepository.findAllById(newDiscountRequest.getBookIds()));
		newDiscount.setGenres(genresRepository.findAllById(newDiscountRequest.getGenreIds()));
		
		discountRepository.save(newDiscount);
	}
	
	@Override
	@Transactional
	public void delete(Long discountId) {
		discountRepository.deleteById(discountId);
	}
	
	@Override
	@Transactional
	public void activateDiscount(Long discountId) {
		Discount discount = discountRepository.findById(discountId)
				.orElseThrow(() -> new DiscountNotFoundException("Discount with id = %d not found.".formatted(discountId)));
		discount.setActive(true);
	}

	@Override
	@Transactional
	public void deactivateDiscount(Long discountId) {
		Discount discount = discountRepository.findById(discountId)
				.orElseThrow(() -> new DiscountNotFoundException("Discount with id = %d not found.".formatted(discountId)));
		discount.setActive(false);
	}
	/**
	 * Validates parameters of a discount creation request.
	 * <p>
	 * Validation rules are:
	 * <ul>
	 *		<li>The discount must apply either to specific books or genres, but not both and not neither.</li>
	 *		<li>If discount type is {@link DiscountType#PERCENT}, the amount must be less than 100.</li>
	 *		<li>The end date must be strictly after the start date</li>
	 * </ul>
	 * @param newDiscountRequest - DTO containing discount details
	 * @throws IllegalArgumentException if any of the validation rules are violated
	 */
	private void validateDiscountCreateRequest(CreateDiscountDto newDiscountRequest) {
		//At least one bookId or genreId but not both
		List<Long> bookIds = newDiscountRequest.getBookIds();
		List<Long> genreIds = newDiscountRequest.getGenreIds();

		if(bookIds.isEmpty() == genreIds.isEmpty()){
			throw new IllegalArgumentException("Discount must apply to either books or genres, but not both or none.");
		}
		
		//Percent amount !> 100
		DiscountType discountType = newDiscountRequest.getType();
		if(discountType.equals(DiscountType.PERCENT)) {
			BigDecimal amount = newDiscountRequest.getAmount();
			BigDecimal oneHundred = new BigDecimal(100);
			if(amount.compareTo(oneHundred) >= 0) {
				throw new IllegalArgumentException("Discount cannot be equal or bigger than 100%");
			}
		}
		
		//End date cannot predate start date
		LocalDateTime startDate = newDiscountRequest.getStartDate();
		LocalDateTime endDate = newDiscountRequest.getEndDate();
		if(!startDate.isBefore(endDate)) {
			throw new IllegalArgumentException("End date cannot predate start date");
		}
	}
	
	private Discount assembleDiscount(CreateDiscountDto dto) {
		Discount discount = new Discount();
		
		discount.setName(dto.getName());
		discount.setDescription(dto.getDescription());
		discount.setAmount(dto.getAmount());
		discount.setType(dto.getType());
		discount.setStartDate(dto.getStartDate());
		discount.setEndDate(dto.getEndDate());
		discount.setActive(dto.isActive());
		
		return discount;
	}

}






















