package shop.services.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.dto.genre.GenreDto;
import shop.mapping.mappers.GenreMapper;
import shop.persistence.repositories.GenresRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{
	
	private final GenresRepository genresRepository;
	private final GenreMapper genreMapper;

	@Override
	public List<GenreDto> getAllGenres() {
		return genresRepository.findAllSortByName();
	}

	@Override
	public Page<GenreDto> getGenres(Pageable pageable) {
		return genresRepository.findAll(pageable).map(genreMapper::toGenreDto);
	}

}
