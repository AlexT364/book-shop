package shop.controllers.images;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.services.image.AuthorImageService;

@RestController
@RequestMapping(path="/images/authors")
@RequiredArgsConstructor
public class AuthorsImagesController {

	private final AuthorImageService authorImageService;
	
	@GetMapping(path="/{authorId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<Resource> getAuthorImage(@PathVariable long authorId){
		Resource authorImage = authorImageService.getImage(authorId);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.IMAGE_JPEG)
				.body(authorImage);
	}
}
