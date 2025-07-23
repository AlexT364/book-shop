package shop.controllers.book;

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
import shop.dto.book.BookDto;
import shop.dto.book.CreateEditBookDto;
import shop.exceptions.book.BookAlreadyExistsException;
import shop.exceptions.image.ImageValidationException;
import shop.services.author.AuthorQueryService;
import shop.services.book.BookService;
import shop.services.genre.GenreService;

@Controller
@RequestMapping(path = "/admin/book")
@RequiredArgsConstructor
public class BookAdminActionController {

	private final BookService bookService;
	private final GenreService genreService;
	private final AuthorQueryService authorQueryService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@PostMapping
	public String addBook(
			@ModelAttribute(name = "book") @Valid CreateEditBookDto bookCreateDto,
			BindingResult bindingResult, 
			MultipartFile coverImage, 
			Model model) {
		
		if (bindingResult.hasErrors()) {
			this.prepareModel(model);
			return "books/book-add.html";
		}

		try {
			this.validateImage(coverImage);
			BookDto newBook = bookService.addBook(bookCreateDto, coverImage.getBytes());
			return "redirect:/book/%d".formatted(newBook.getId());
		} catch (IOException e) {
			this.prepareModel(model);
			model.addAttribute("coverImageError", "Could not process image file.");
			return "books/book-add.html";
		} catch (BookAlreadyExistsException e) {
			this.prepareModel(model);
			bindingResult.rejectValue("isbn", "isbn.exists", e.getMessage());
			return "books/book-add.html";
		}
	}

	@PatchMapping(path = "/{bookId:\\d+}")
	public String editBook(
			@PathVariable long bookId,
			@ModelAttribute(name = "book") @Valid CreateEditBookDto bookUpdateDto,
			BindingResult bindingResult,
			MultipartFile coverImage,
			Model model){
		if (bindingResult.hasErrors()) {
			this.prepareModel(model);
			return "books/book-edit.html";
		}
		
		try {
			this.validateImage(coverImage);
			BookDto newBook = bookService.updateBook(bookId, bookUpdateDto, coverImage.getBytes());
			return "redirect:/book/%d".formatted(newBook.getId());
		} catch (IOException e) {
			this.prepareModel(model);
			model.addAttribute("coverImageError", "Could not process image file.");
			return "books/book-edit.html";
		}
		
		
	}
	
	@DeleteMapping(path = "/{bookId:\\d+}")
	public String deleteBook(@PathVariable long bookId) {
		bookService.deleteBook(bookId);
		return "redirect:/shop";
	}
	
	@ExceptionHandler(exception = ImageValidationException.class)
	public String handleImageValidationException(ImageValidationException e, Model model, HttpServletRequest request) {
		this.prepareModel(model);
		model.addAttribute("coverImageError", e.getMessage());
		
		String method = request.getMethod();
		if(method.equalsIgnoreCase("POST")) {
			return "books/book-add.html";
		}
		return "books/book-edit.html";
		
	}
	
	private void prepareModel(Model model) {
		model.addAttribute("genres", genreService.getAllGenres());
		model.addAttribute("authors", authorQueryService.getAllAuthors());
	}
	
	private void validateImage(MultipartFile coverImageFile){
		if(coverImageFile == null || coverImageFile.isEmpty()) {
			return;
		}
		
		String contentType = coverImageFile.getContentType();
		if(contentType == null) {
			throw new ImageValidationException("Image should be a jpeg/jpg file.");
		}
		
		if(!(contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/jpg"))) {
			throw new ImageValidationException("Image should be a jpeg/jpg file.");
		}
		
		long maxSizeInBytes = 3 * 1024 * 1024; // 3 MB
		if(coverImageFile.getSize() > maxSizeInBytes) {
			throw new ImageValidationException("Image should be no bigger than 3 MB.");
		}
	}

}
