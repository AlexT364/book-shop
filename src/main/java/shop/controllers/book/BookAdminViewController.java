package shop.controllers.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.dto.book.CreateEditBookDto;
import shop.services.author.AuthorQueryService;
import shop.services.book.BookQueryService;
import shop.services.genre.GenreService;

@Controller
@RequestMapping(path = "/admin/book")
@RequiredArgsConstructor
public class BookAdminViewController {

	private final BookQueryService bookQueryService;
	private final GenreService genresService;
	private final AuthorQueryService authorQueryService;
	
	@ModelAttribute
	public void prepareModel(
			@PathVariable(required = false) Long bookId, 
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "5") int pageSize,
			Model model
			) {
		CreateEditBookDto book = bookId == null ? 
				new CreateEditBookDto() : 
				bookQueryService.getBookByIdForEdit(bookId);
		

		model.addAttribute("book", book);
		model.addAttribute("genres", genresService.getAllGenres());
		model.addAttribute("authors", authorQueryService.getAllAuthorsShort());
	}
	
	@GetMapping(path = "/add")
	public String bookAddPage(Model model) {
		return "books/book-add.html";
	}
	
	@GetMapping(path = "/{bookId:\\d+}/edit")
	public String bookEditPage(@PathVariable Long bookId, Model model) {
		return "books/book-edit.html";
	}
}
