package shop.services.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.genre.GenreDto;

public interface GenreService {
	
	public List<GenreDto> getAllGenres();

	public Page<GenreDto> getGenres(Pageable pageable);
}
