package shop.mapping.converters.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.book.ShortBookDto;
import shop.persistence.entities.Book;

public class BookToShortDtoConverter implements Converter<Book, ShortBookDto>{

	@Override
	public ShortBookDto convert(MappingContext<Book, ShortBookDto> context) {
		ShortBookDto dto = new ShortBookDto();
		Book entity = context.getSource();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		dto.setUnitsAvailable(entity.getUnitsInStock() - entity.getUnitsReserved());
		return dto;
	}
	

}
