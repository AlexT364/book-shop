package shop.controllers.genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.dto.genre.GenreDto;
import shop.services.genre.GenreService;

@RestController
@RequestMapping(path = "/api/genres")
@RequiredArgsConstructor
public class GenreRestController {

	private final GenreService genreService;

	@GetMapping
	public ResponseEntity<Page<GenreDto>> loadGenres(
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "5") int pageSize) {
		Page<GenreDto> genres = genreService.getGenres(PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending()));
		
		return ResponseEntity.status(HttpStatus.OK).body(genres);
	}
}
