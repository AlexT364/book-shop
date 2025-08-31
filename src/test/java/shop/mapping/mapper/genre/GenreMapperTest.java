package shop.mapping.mapper.genre;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import shop.dto.genre.GenreDto;
import shop.mapping.mappers.genre.GenreMapper;
import shop.persistence.entities.Genre;

public class GenreMapperTest {
	
	private GenreMapper genreMapper = new GenreMapper();
	
	@Test
	void toGenreDto_shouldMapEntityToGenreDto() {
		Genre entity = new Genre();
		entity.setId(1L);
		entity.setName("Fiction");
		entity.setDiscounts(Collections.emptySet());
		
		GenreDto result = genreMapper.toGenreDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getName()).isEqualTo("Fiction");
	}
}
