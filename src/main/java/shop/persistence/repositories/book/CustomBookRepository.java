package shop.persistence.repositories.book;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.ShopRequestDto;
import shop.persistence.entities.Book;

public interface CustomBookRepository {
	Page<Book> findCriteriaBooks(Pageable pageable, ShopRequestDto request, boolean onlyAvailable);
	
	Optional<Book> findByIdWithAuthorsAndGenres(Long id);
	
	BigDecimal findMaxPriceWithFilters(ShopRequestDto shopRequestDto);

	
}
