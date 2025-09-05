package shop.dto.populators;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;
import shop.dto.book.ShortBookDto;
import shop.persistence.entities.Discount;
import shop.services.book.cache.BookCacheService;
import shop.services.discount.DiscountPriceCalculator;
import shop.services.discount.DiscountQueryService;
import shop.services.favourite.FavouriteService;
import shop.services.reviews.BookReviewService;

@Component
@RequiredArgsConstructor
public class BookDtoPopulator {

	private final FavouriteService favouriteService;
	private final BookCacheService bookCacheService;
	private final DiscountQueryService discountQueryService;
	private final DiscountPriceCalculator discountPriceCalculator;
	private final BookReviewService bookReviewService;
	
	public void populateBookDto(BookDto dtoTopopulate) {
		populateBookDtoInternal(dtoTopopulate, null);
	}
	
	public void populateBookDto(BookDto dtoTopopulate, String username) {
		populateBookDtoInternal(dtoTopopulate, username);
	}
	
	private void populateBookDtoInternal(BookDto dtoToPopulate, String username) {
		if(username != null) {
			populateWithFavourite(dtoToPopulate, username);
		}else {
			dtoToPopulate.setFavourite(false);
		}
		populateWithScore(dtoToPopulate);
		populateWithDiscount(dtoToPopulate);
	}
	
	public void populateShortBookDtos(Collection<ShortBookDto> dtosToPopulate) {
		populateShortBookDtosInternal(dtosToPopulate, null);
	}
	
	public void populateShortBookDtos(Collection<ShortBookDto> dtosToPopulate, String username) {
		populateShortBookDtosInternal(dtosToPopulate, username);
	}
	
	private void populateShortBookDtosInternal(Collection<ShortBookDto> dtosToPopulate, String username) {
		List<Long> bookIds = dtosToPopulate.stream()
				.map(ShortBookDto::getId)
				.toList();
		
		if(username != null) {
			populateWithFavourites(dtosToPopulate, bookIds, username);
		}else {
			dtosToPopulate.forEach(dto -> dto.setFavourite(false));
		}
		populateWithScores(dtosToPopulate, bookIds);
		populateWithDiscounts(dtosToPopulate, bookIds);
	}
	
	//-- Helper methods --
	private void populateWithScore(BookDto dto) {
		Long bookId = dto.getId();
		Double score = bookCacheService.getCachedScore(bookId)
				.orElseGet(() -> {
					double avg = bookReviewService.findAvgScoreForBook(bookId).orElse(0.0);
					if(avg != 0.0) {
						bookCacheService.cacheScore(bookId, avg);
					}
					return avg;
				});
		dto.setScore(score);
	}
	
	private void populateWithFavourite(BookDto dto, String username) {
		boolean isFavourite = favouriteService.isFavourite(username, dto.getId());
		dto.setFavourite(isFavourite);
	}
	
	private void populateWithDiscount(BookDto dto) {
		Set<Discount> discounts = discountQueryService.findDiscountsForBook(dto.getId());
		BigDecimal priceWithDiscount = discountPriceCalculator.calculateBestPrice(discounts, dto.getPrice());
		dto.setPriceWithDiscount(priceWithDiscount.compareTo(dto.getPrice()) < 0 ? priceWithDiscount : null);
	}
	
	private void populateWithScores(Collection<ShortBookDto> dtos, List<Long> bookIds) {
		Map<Long, Double> booksScores = bookReviewService.findAvgScoresForBooks(bookIds);
		dtos.forEach(dto -> dto.setScore(booksScores.getOrDefault(dto.getId(), 0.0)));
	}
	
	private void populateWithFavourites(Collection<ShortBookDto> dtos, List<Long> bookIds, String username) {
		Set<Long> favouriteIds = favouriteService.findFavouriteBookIdsForUser(username, bookIds);
		dtos.forEach(dto -> dto.setFavourite(favouriteIds.contains(dto.getId())));
	}
	
	private void populateWithDiscounts(Collection<ShortBookDto> dtos, List<Long> bookIds) {
		Map<Long, Set<Discount>> discountsByBookId = discountQueryService.findDiscountsForBooks(bookIds);
		dtos.forEach(dto ->{
			Set<Discount> applicableDiscounts = discountsByBookId.getOrDefault(dto.getId(), Set.of());
			BigDecimal basePrice = dto.getPrice();
			BigDecimal priceWithDiscount = discountPriceCalculator.calculateBestPrice(applicableDiscounts, basePrice);
			
			dto.setPriceWithDiscount(priceWithDiscount.compareTo(dto.getPrice()) < 0 ? priceWithDiscount : null);
		});
	}
}
