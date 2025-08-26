package shop.mapping.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Genre;

@Component
@RequiredArgsConstructor
public class BookMapper{
	
	private final AuthorMapper authorMapper;
	private final GenreMapper genreMapper;
	
	public BookDto toBookDto(Book entity) {
		BookDto dto = new BookDto();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		
		List<AuthorDto> authorDtos = entity.getAuthors().stream().map(author -> authorMapper.toAuthorDto(author)).toList();
		List<GenreDto> genresDtos = entity.getGenres().stream().map(genreMapper::toGenreDto).toList();
		dto.setAuthors(authorDtos);
		dto.setGenres(genresDtos);
		
		return dto;
	}
	
	public CreateEditBookDto toCreateEditDto(Book entity) {
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
	
	public ShortBookDto toShortDto(Book entity) {
		ShortBookDto dto = new ShortBookDto();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		
		dto.setUnitsAvailable(entity.getUnitsInStock() - entity.getUnitsReserved());
		
		return dto;
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
	
}
