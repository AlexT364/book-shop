package shop.mapping.mappers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.persistence.entities.Book;

@Component
@RequiredArgsConstructor
public class BookMapper{
	
	private final AuthorMapper authorMapper;
	private final ModelMapper modelMapper;
	
	public BookDto toBookDto(Book entity) {
		BookDto dto = modelMapper.map(entity, BookDto.class);
		List<AuthorDto> authorDtos = entity.getAuthors().stream().map(author -> authorMapper.toAuthorDto(author)).toList();
		List<GenreDto> genresDtos = entity.getGenres().stream().map(genre -> modelMapper.map(genre, GenreDto.class)).toList();
		dto.setAuthors(authorDtos);
		dto.setGenres(genresDtos);
		return dto;
	}
	
	public CreateEditBookDto toCreateEditDto(Book entity) {
		return modelMapper.map(entity, CreateEditBookDto.class);
	}
	
	public ShortBookDto toShortDto(Book entity) {
		return modelMapper.map(entity, ShortBookDto.class);
	}
	
	public void map(Book source, Book destination) {
		modelMapper.map(source, destination);
	}
	
	public void mapForUpdate(CreateEditBookDto source, Book destination) {
		destination.setTitle(source.getTitle());
		destination.setDescription(source.getDescription());
		destination.setPrice(source.getPrice());
		destination.setUnitsInStock(source.getUnitsInStock());
	}
	
	public Book mapForCreate(CreateEditBookDto dto) {
		Book entity = new Book();
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setIsbn(dto.getIsbn());
		entity.setPrice(dto.getPrice());
		entity.setUnitsInStock(dto.getUnitsInStock());
		entity.setUnitsReserved(0);
		return entity;
	}
	
	public Book toEntity(BookDto dto) {
		return modelMapper.map(dto, Book.class);
	}
	
}
