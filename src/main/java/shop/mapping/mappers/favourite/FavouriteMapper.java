package shop.mapping.mappers.favourite;

import org.springframework.stereotype.Component;

import shop.dto.book.ShortBookDto;
import shop.persistence.entities.Book;
import shop.persistence.entities.Favourite;

@Component
public class FavouriteMapper {
	
	public ShortBookDto toShortBookDto(Favourite entity) {
		ShortBookDto dto = new ShortBookDto();
		Book book = entity.getBook();
		
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setIsbn(book.getIsbn());
		dto.setPrice(book.getPrice());
		dto.setUnitsAvailable(book.getUnitsInStock() - book.getUnitsReserved());
		
		return dto;
	}
}
