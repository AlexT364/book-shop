package shop.controllers.author;

import java.io.IOException;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.exceptions.image.ImageValidationException;
import shop.services.author.AuthorService;

@Controller
@RequestMapping(path="/admin/authors")
@RequiredArgsConstructor
public class AuthorAdminActionController {
	
	private final AuthorService authorService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@PostMapping
	public String addAuthor(
			@Valid @ModelAttribute("author") CreateEditAuthorDto authorDto,
			BindingResult bindingResult,
			MultipartFile authorImage,
			Model model) {
		if(bindingResult.hasErrors()) {
			return "authors/author-add.html";
		}
		
		this.validateImage(authorImage);
		
		try {
			AuthorDto author = authorService.addAuthor(authorDto, authorImage.getBytes());
			return "redirect:/authors/%d".formatted(author.getId());
		} catch (IOException e) {
			model.addAttribute("authorImageError", "Could not process image file.");
			return "books/book-edit.html";
		}
		
	}
	
	@PatchMapping(path = "/{authorId:\\d+}")
	public String editAuthor(
			@PathVariable Long authorId,
			@Valid @ModelAttribute("author") CreateEditAuthorDto authorDto,
			BindingResult bindingResult,
			MultipartFile authorImage,
			Model model){
		
		if(bindingResult.hasErrors()) {
			return "authors/author-edit.html";
		}
		
		this.validateImage(authorImage);
		
		try {
			AuthorDto author = authorService.updateAuthor(authorId, authorDto, authorImage.getBytes());
			return "redirect:/authors/%d".formatted(author.getId());
		}catch(IOException e) {
			model.addAttribute("authorImageError", "Could not process image file.");
			return "authors/author-edit.html";
		}
	}
	
	@DeleteMapping(path="/{authorId:\\d+}")
	public String deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);
		return "redirect:/authors";
	}
	
	@ExceptionHandler(exception = ImageValidationException.class)
	public String handleImageValidationException(ImageValidationException e, Model model, HttpServletRequest request) {
		model.addAttribute("authorImageError", e.getMessage());
		
		String method = request.getMethod();
		if(method.equalsIgnoreCase("POST")) {
			return "authors/author-add.html";
		}
		return "authors/author-edit.html";
		
	}
	
	private void validateImage(MultipartFile authorImageFile){
		if(authorImageFile == null || authorImageFile.isEmpty()) {
			return;
		}
		
		String contentType = authorImageFile.getContentType();
		if(contentType == null) {
			throw new ImageValidationException("Image should be a jpeg/jpg file.");
		}
		
		if(!(contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/jpg"))) {
			throw new ImageValidationException("Image should be a jpeg/jpg file.");
		}
		
		long maxSizeInBytes = 3 * 1024 * 1024; // 3 MB
		if(authorImageFile.getSize() > maxSizeInBytes) {
			throw new ImageValidationException("Image should be no bigger than 3 MB.");
		}
	}

	

}
