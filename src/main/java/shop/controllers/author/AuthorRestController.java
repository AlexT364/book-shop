package shop.controllers.author;

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
import shop.dto.author.ShortAuthorDto;
import shop.services.author.AuthorQueryService;

@RestController
@RequestMapping(path = "/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

	private final AuthorQueryService authorQueryService;
	
	@GetMapping
	public ResponseEntity<Page<ShortAuthorDto>> loadAuthors(
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "5") int pageSize){
		Page<ShortAuthorDto> authors = authorQueryService.getAuthors(PageRequest.of(pageNumber, pageSize, Sort.by("lastName").ascending()));
		
		return ResponseEntity.status(HttpStatus.OK).body(authors);
	}
}
