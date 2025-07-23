package shop.services.favourite;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import shop.dto.ShopRequestDto;
import shop.dto.author.ShortAuthorDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;

public interface FavouriteService {

	Page<ShortBookDto> getFavouriteBooks(ShopRequestDto request, String username);
	
	BigDecimal getMaxPriceOfFavourites(ShopRequestDto request, String username);
	
	void addBook(String username, Long bookId);

	void deleteBook(Long bookId, String username);

	Set<Long> findFavouriteBookIdsForUser(String username, List<Long> bookIds);

	boolean checkIfBookInUsersFavourites(String username, Long bookId);

	List<GenreDto> getDistinctGenresInUserFavourites(String username);
	
	List<ShortAuthorDto> getDistinctAuthorsInUserFavourites(String username);

}
