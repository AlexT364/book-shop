package shop.persistence.repositories.favourite;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.ShopRequestDto;
import shop.persistence.entities.Favourite;

public interface CustomFavouriteRepository {
	Page<Favourite> findFavouriteBooks(ShopRequestDto request, String username, Pageable pageable);
	
	BigDecimal findMaxPriceOfFavourites(ShopRequestDto request, String username, Pageable pageable);

}
