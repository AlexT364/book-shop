package shop.mapping.mappers.genre;

import org.springframework.stereotype.Component;

import shop.dto.genre.GenreDto;
import shop.persistence.entities.Genre;

@Component
public class GenreMapper {

	public GenreDto toGenreDto(Genre entity) {
		GenreDto dto = new GenreDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		
		return dto;
	}
}
