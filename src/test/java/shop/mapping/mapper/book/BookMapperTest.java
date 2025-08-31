package shop.mapping.mapper.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shop.dto.author.AuthorDto;
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.mapping.mappers.author.AuthorMapper;
import shop.mapping.mappers.book.BookMapper;
import shop.mapping.mappers.genre.GenreMapper;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Genre;

@ExtendWith(MockitoExtension.class)
public class BookMapperTest {
	
	@InjectMocks
	private BookMapper bookMapper;
	@Mock
	private AuthorMapper authorMapper;
	@Mock
	private GenreMapper genreMapper;
	
	@Test
	void toBookDto_shouldMapEntityToBookDto() {
		Book entity = prepareEntity();
		
		when(authorMapper.toAuthorDto(any(Author.class))).thenReturn(new AuthorDto());
		when(genreMapper.toGenreDto(any(Genre.class))).thenReturn(new GenreDto());
		
		BookDto result = bookMapper.toBookDto(entity);

		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTitle()).isEqualTo("War and Peace");
		assertThat(result.getDescription()).isEqualTo("War and Peace description");
		assertThat(result.getIsbn()).isEqualTo("1234567890");
		assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(21.22));
		assertThat(result.getUnitsAvailable()).isEqualTo(5);
		
		verify(authorMapper).toAuthorDto(any(Author.class));
		verify(genreMapper).toGenreDto(any(Genre.class));
		verifyNoMoreInteractions(authorMapper, genreMapper);
	}
	
	@ParameterizedTest
	@MethodSource("toBookDto_prepareBooksForMapping")
	void toBookDto_shouldMapEntitiesWithDifferentAuthorsAndGenres(
			Book entity,
			List<AuthorDto> expectedAuthors,
			List<GenreDto> expectedGenres
			) {
		
		entity.getAuthors().forEach(author -> {
			AuthorDto dto = new AuthorDto(author.getId(), null, null);
			when(authorMapper.toAuthorDto(author)).thenReturn(dto);
		});
		
		entity.getGenres().forEach(genre -> {
			GenreDto dto = new GenreDto(genre.getId(), null);
			when(genreMapper.toGenreDto(genre)).thenReturn(dto);
		});
		
		BookDto result = bookMapper.toBookDto(entity);
		
		assertThat(result.getAuthors())
			.extracting(AuthorDto::getId)
			.containsExactlyInAnyOrderElementsOf((expectedAuthors.stream().map(AuthorDto::getId).toList()));
		
		assertThat(result.getGenres())
			.extracting(GenreDto::getId)
			.containsExactlyInAnyOrderElementsOf(expectedGenres.stream().map(GenreDto::getId).toList());
	}
	
	@Test
	void toCreateEditDto_shouldMapEntityToCreateEditBookDto() {
		Book entity = prepareEntity();
		
		CreateEditBookDto result = bookMapper.toCreateEditDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTitle()).isEqualTo("War and Peace");
		assertThat(result.getDescription()).isEqualTo("War and Peace description");
		assertThat(result.getIsbn()).isEqualTo("1234567890");
		assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(21.22));
		assertThat(result.getUnitsInStock()).isEqualTo(10);
		
	}
	
	@ParameterizedTest
	@MethodSource("toCreateEditDto_prepareBooksForMapping")
	void toCreateEditDto_shouldCorrectlyMapEntityAuthorsAndGenresToTheirIds(
			Book entity, 
			List<Long> expectedAuthorIds,
			List<Long> expectedGenreIds) {
		
		CreateEditBookDto result = bookMapper.toCreateEditDto(entity);
		
		assertThat(result.getAuthors()).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
		assertThat(result.getGenres()).containsExactlyInAnyOrderElementsOf(expectedGenreIds);
	}
	
	@Test
	void toSortDto_shouldMapEntityToShortBookDto() {
		Book entity = prepareEntity();
		
		ShortBookDto result = bookMapper.toShortDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTitle()).isEqualTo("War and Peace");
		assertThat(result.getIsbn()).isEqualTo("1234567890");
		assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(21.22));
		assertThat(result.getUnitsAvailable()).isEqualTo(5);
	}
	
	@Test
	void mapForUpdate_shouldMapOnlyUpdatableFields() {
		Book destination = prepareEntity();
		CreateEditBookDto source = new CreateEditBookDto();
		source.setId(2L);
		source.setTitle("Dto Title");
		source.setDescription("Dto Description");
		source.setIsbn("0987654321");
		source.setPrice(BigDecimal.valueOf(100));
		source.setUnitsInStock(100);
		source.setAuthors(Set.of());
		source.setGenres(Set.of());
		
		bookMapper.mapForUpdate(source, destination);
		
		//Fields that shouldn't be mapped
		assertThat(destination.getId()).isEqualTo(1L);
		assertThat(destination.getIsbn()).isEqualTo("1234567890");
		assertThat(destination.getUnitsReserved()).isEqualTo(5);
		assertThat(destination.getAddedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 0, 0));
		assertThat(destination.getAuthors())
			.extracting(Author::getId)
			.containsExactlyInAnyOrder(1L);
		assertThat(destination.getGenres())
			.extracting(Genre::getId)
			.containsExactlyInAnyOrder(10L);
		
		//Fields that should be mapped
		assertThat(destination.getTitle()).isEqualTo("Dto Title");
		assertThat(destination.getDescription()).isEqualTo("Dto Description");
		assertThat(destination.getPrice()).isEqualTo(BigDecimal.valueOf(100));
		assertThat(destination.getUnitsInStock()).isEqualTo(100);
	}
	
	@Test
	void mapForCreate_shouldMapOnlyFieldsNeededForCreation() {
		CreateEditBookDto dto = new CreateEditBookDto();
		dto.setId(1L);	
		dto.setTitle("War and Peace");
		dto.setDescription("War and Peace description");
		dto.setIsbn("1234567890");
		dto.setPrice(BigDecimal.valueOf(21.22));
		dto.setUnitsInStock(10);
		dto.setAuthors(Set.of(1L));
		dto.setGenres(Set.of(10L));
		
		Book result = bookMapper.mapForCreate(dto);
		
		//Fields that should be mapped
		assertThat(result.getTitle()).isEqualTo("War and Peace");
		assertThat(result.getDescription()).isEqualTo("War and Peace description");
		assertThat(result.getIsbn()).isEqualTo("1234567890");
		assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(21.22));
		assertThat(result.getUnitsInStock()).isEqualTo(10);
		
		//Fields that shouldn't be mapped
		assertThat(result.getId()).isNull();
		assertThat(result.getAuthors()).isEmpty();
		assertThat(result.getGenres()).isEmpty();
		assertThat(result.getDiscounts()).isEmpty();
		assertThat(result.getUnitsReserved()).isNull();
		assertThat(result.getAddedAt()).isNull();
	}
	
	static Book prepareEntity() {
		Book entity = new Book();
		entity.setId(1L);
		entity.setTitle("War and Peace");
		entity.setDescription("War and Peace description");
		entity.setIsbn("1234567890");
		entity.setPrice(BigDecimal.valueOf(21.22));
		entity.setUnitsInStock(10);
		entity.setUnitsReserved(5);
		entity.setAddedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
		
		Author a = new Author(); 
		a.setId(1L);
		Genre g = new Genre(); 
		g.setId(10L);
		entity.setAuthors(Set.of(a));
		entity.setGenres(Set.of(g));
		return entity;
	}
	
	static Stream<Arguments> toCreateEditDto_prepareBooksForMapping(){
		Author a1 = new Author();
		a1.setId(1L);
		Author a2 = new Author();
		a2.setId(2L);
		
		Genre g1 = new Genre();
		g1.setId(10L);
		Genre g2 = new Genre();
		g2.setId(20L);
		
		Book b1 = new Book();
		b1.setAuthors(Set.of());
		b1.setGenres(Set.of());
		Book b2 = new Book();
		b2.setAuthors(Set.of(a1));
		b2.setGenres(Set.of(g1));
		Book b3 = new Book();
		b3.setAuthors(Set.of(a1, a2));
		b3.setGenres(Set.of(g1, g2));
		
		return Stream.of(
					Arguments.of(b1, List.of(), List.of()),
					Arguments.of(b2, List.of(1L), List.of(10L)),
					Arguments.of(b3, List.of(1L, 2L), List.of(10L, 20L))
				);
	}
	
	static Stream<Arguments> toBookDto_prepareBooksForMapping(){
		Author a1 = new Author();
		a1.setId(1L);
		Author a2 = new Author();
		a2.setId(2L);
		
		Genre g1 = new Genre();
		g1.setId(10L);
		Genre g2 = new Genre();
		g2.setId(20L);
		
		Book b1 = new Book();
		b1.setAuthors(Set.of());
		b1.setGenres(Set.of());
		b1.setUnitsInStock(10);
		b1.setUnitsReserved(5);
		Book b2 = new Book();
		b2.setAuthors(Set.of(a1));
		b2.setGenres(Set.of(g1));
		b2.setUnitsInStock(10);
		b2.setUnitsReserved(5);
		Book b3 = new Book();
		b3.setAuthors(Set.of(a1, a2));
		b3.setGenres(Set.of(g1, g2));
		b3.setUnitsInStock(10);
		b3.setUnitsReserved(5);
		
		return Stream.of(
				Arguments.of(
						b1,
						List.of(),
						List.of()
						),
				Arguments.of(
						b2,
						List.of(new AuthorDto(1L, null, null)),
						List.of(new GenreDto(10L, null))
						),
				Arguments.of(
						b3,
						List.of(new AuthorDto(2L, null, null), new AuthorDto(1L, null, null)),
						List.of(new GenreDto(10L, null), new GenreDto(20L, null))
						)
				);
	}
}






















