package shop.controllers.images;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.services.image.CoversImageService;

@RestController
@RequestMapping(path="/images/books")
@RequiredArgsConstructor
public class CoversImagesController {
	
	private final CoversImageService coversImageService;
	
	@GetMapping(path = "/{isbn}")
	public ResponseEntity<Resource> getCoverImage(@PathVariable String isbn) throws IOException, URISyntaxException {
		
		Resource imageResource = coversImageService.getImage(isbn);
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.IMAGE_JPEG)
				.body(imageResource);
	}
}
