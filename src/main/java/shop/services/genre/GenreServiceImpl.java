package shop.services.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.dto.genre.GenreDto;
import shop.persistence.repositories.GenresRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{
	
	private final GenresRepository jpaGenresRepository;

	@Override
	public List<GenreDto> getAllGenres() {
		return jpaGenresRepository.findAllSortByName();
	}

	@Override
	public Page<GenreDto> getGenres(Pageable pageable) {
		return jpaGenresRepository.findAll(pageable).map(genre -> new GenreDto(genre.getId(), genre.getName()));
	}

}
