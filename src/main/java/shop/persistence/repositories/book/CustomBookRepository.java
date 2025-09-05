package shop.persistence.repositories.book;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.ShopRequestDto;
import shop.persistence.entities.Book;

public interface CustomBookRepository {
	Page<Book> findCriteriaBooks(Pageable pageable, ShopRequestDto request, boolean onlyAvailable);
	
	BigDecimal findMaxPriceWithFilters(ShopRequestDto shopRequestDto);

	
}
