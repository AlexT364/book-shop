package shop.mapping.mappers.book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.mapping.mappers.author.AuthorMapper;
import shop.mapping.mappers.genre.GenreMapper;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Genre;

@Component
@RequiredArgsConstructor
public class BookMapper{
	
	private final AuthorMapper authorMapper;
	private final GenreMapper genreMapper;
	
	/**
	 * Converts a {@link Book} entity to {@link BookDto}.
	 * <p>
	 * The following fields are intentionally <b>not</b> mapped by this method,
	 * as they are populated by business logic at service layer:
	 * <ul>
	 * 		<li>{@code priceWithDiscount}</li>
	 * 		<li>{@code score}</li>
	 * 		<li>{@code favourite}</li>
	 * </ul>
	 * <p>Additionally, this method maps nested collections of:
	 * <ul>
	 * 		<li>{@link Book#getAuthors()} to List<{@link AuthorDto}></li>
	 * 		<li>{@link Book#getGenres()} to List<{@link GenreDto}></li>
	 * </ul>
	 * @param entity - the {@link Book} entity to map; must not be {@code null}
	 * @return a new @{link BookDto} with mapped fields; never {@code null}
	 * @throws NullPointerException if {@code entity} is null
	 */
	public BookDto toBookDto(Book entity) {
		BookDto dto = new BookDto();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		dto.setUnitsAvailable(entity.getUnitsInStock() - entity.getUnitsReserved());
		
		List<AuthorDto> authorDtos = entity.getAuthors().stream().map(authorMapper::toAuthorDto).toList();
		List<GenreDto> genresDtos = entity.getGenres().stream().map(genreMapper::toGenreDto).toList();
		dto.setAuthors(authorDtos);
		dto.setGenres(genresDtos);
		
		return dto;
	}
	
	/**
	 * Converts a {@link Book} entity to {@link CreateEditBookDto}.
	 *
	 * @param entity - the {@link Book} entity to map; must not be {@code null}
	 * @return a new {@link CreateEditBookDto} with mapped fields; never {@code null}
	 * @throws NullPointerException if {@code entity} is null
	 */
	public CreateEditBookDto toCreateEditDto(Book entity) {
		CreateEditBookDto dto = new CreateEditBookDto();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		dto.setUnitsInStock(entity.getUnitsInStock());
		
		dto.setAuthors(entity.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
		dto.setGenres(entity.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
		
		return dto;
	}
	
	/**
	 * Converts a {@link Book} entity to {@link ShortBookDto}
	 * <p>
	 * The following fields are intentionally <b>not</b> mapped by this method,
	 * as they are populated by business logic at service layer:
	 * <ul>
	 * 		<li>{@code priceWithDiscount}</li>
	 * 		<li>{@code score}</li>
	 * 		<li>{@code favourite}</li>
	 * </ul>
	 * @param entity - the {@link Book} entity to map; must not be {@code null}
	 * @return a new {@link ShortBookDto} with mapped fields; never {@code null}
	 * @throws NullPointerException if {@code entity} is null
	 */
	public ShortBookDto toShortDto(Book entity) {
		ShortBookDto dto = new ShortBookDto();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setIsbn(entity.getIsbn());
		dto.setPrice(entity.getPrice());
		
		dto.setUnitsAvailable(entity.getUnitsInStock() - entity.getUnitsReserved());
		
		return dto;
	}
	
	/**
	 * Updates mutable fields of the given {@link Book} entity
	 * using values from the provided {@link CreateEditBookDto}.
	 * <p>
	 * <ul>Only the following fields are updated:
	 * 		<li>{@code title}</li>
	 * 		<li>{@code description}</li>
	 * 		<li>{@code price}</li>
	 * 		<li>{@code unitsInStock}</li>
	 * </ul>
	 * Other fields remain unchanged.
	 * 
	 * @param source - a {@link CreateEditBookDto} object with updated fields; must not be {@code null}
	 * @param destination - a {@link Book} entity to update; must not be {@code null}
	 * @throws NullPointerException if {@code source} or {@code destination} is {@code null}
	 */
	public void mapForUpdate(CreateEditBookDto source, Book destination) {
		destination.setTitle(source.getTitle());
		destination.setDescription(source.getDescription());
		destination.setPrice(source.getPrice());
		destination.setUnitsInStock(source.getUnitsInStock());
	}
	
	
	/**
	 * Converts {@link CreateEditBookDto} to {@link Book} entity.
	 * <p>
	 * <ul>The following fields are not mapped:
	 * 		<li>{@code id}</li>
	 * 		<li>{@code unitsReserved}</li>
	 * 		<li>{@code addedAt}</li>
	 * 		<li>{@code authors}</li>
	 * 		<li>{@code genres}</li>
	 * </ul>
	 * 
	 * @param dto - a {@link CreateEditBookDto} object with fields needed for creation of {@link Book} entity;
	 * must not be {@code null}
	 * @return a new {@link Book} with mapped fields; never {@code null}
	 * @throws NullPointerException if {@code dto} is {@code null}
	 */
	public Book mapForCreate(CreateEditBookDto dto) {
		Book entity = new Book();
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setIsbn(dto.getIsbn());
		entity.setPrice(dto.getPrice());
		entity.setUnitsInStock(dto.getUnitsInStock());
		return entity;
	}
	
}
