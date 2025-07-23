package shop.controllers.images;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import shop.services.image.AuthorImageService;

@Controller
@RequestMapping(path="/admin/images/authors")
@RequiredArgsConstructor
public class AuthorsImagesAdminController {

	private final AuthorImageService authorImageService;
	
	@DeleteMapping(path="/{authorId}")
	public String deleteAuthorImage(@PathVariable Long authorId) {
		authorImageService.deleteImage(authorId);
		
		return "redirect:/admin/authors/%d".formatted(authorId);
	}

}
