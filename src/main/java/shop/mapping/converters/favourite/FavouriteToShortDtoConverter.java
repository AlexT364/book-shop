package shop.mapping.converters.favourite;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.book.ShortBookDto;
import shop.persistence.entities.Favourite;

public class FavouriteToShortDtoConverter implements Converter<Favourite, ShortBookDto>{

	@Override
	public ShortBookDto convert(MappingContext<Favourite, ShortBookDto> context) {
		ShortBookDto dto = new ShortBookDto();
		Favourite entity = context.getSource();
		
		dto.setId(entity.getBook().getId());
		dto.setTitle(entity.getBook().getTitle());
		dto.setIsbn(entity.getBook().getIsbn());
		dto.setPrice(entity.getBook().getPrice());
		dto.setUnitsAvailable(entity.getBook().getUnitsInStock() - entity.getBook().getUnitsReserved());
		
		return dto;
	}

}
