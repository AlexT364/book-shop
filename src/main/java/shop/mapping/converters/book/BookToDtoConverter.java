package shop.mapping.converters.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.book.BookDto;
import shop.persistence.entities.Book;

public class BookToDtoConverter implements Converter<Book, BookDto>{

	@Override
	public BookDto convert(MappingContext<Book, BookDto> context) {
		BookDto dto = new BookDto();
		Book entity = context.getSource();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setPrice(entity.getPrice());
		
		dto.setDescription(entity.getDescription());
		dto.setIsbn(entity.getIsbn());
		
		
		return dto;
	}

	
}
