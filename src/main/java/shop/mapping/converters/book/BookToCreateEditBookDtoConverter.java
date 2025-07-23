package shop.mapping.converters.book;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.book.CreateEditBookDto;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Genre;

public class BookToCreateEditBookDtoConverter implements Converter<Book, CreateEditBookDto>{

	@Override
	public CreateEditBookDto convert(MappingContext<Book, CreateEditBookDto> context) {
		Book entity = context.getSource();
		CreateEditBookDto dto = new CreateEditBookDto();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setIsbn(entity.getIsbn());
		dto.setUnitsInStock(entity.getUnitsInStock());
		dto.setPrice(entity.getPrice());
		
		dto.setAuthors(entity.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
		dto.setGenres(entity.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
		
		return dto;
	}

}
