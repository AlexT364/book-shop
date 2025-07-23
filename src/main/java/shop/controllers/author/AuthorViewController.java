package shop.controllers.author;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.author.AuthorDto;
import shop.dto.author.AuthorFilterRequestDto;
import shop.dto.author.ShortAuthorDto;
import shop.services.author.AuthorQueryService;
import shop.services.book.BookQueryService;


@Controller
@RequestMapping(path="/authors")
@RequiredArgsConstructor
public class AuthorViewController extends UriAwareController{
	
	private final AuthorQueryService authorQueryService;
	private final BookQueryService bookQueryService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String getAuthorsPage(
			@ModelAttribute(name = "authorFilter") AuthorFilterRequestDto authorFilterRequest,
			Model model) {
		authorFilterRequest.normalizePagination();
		Page<ShortAuthorDto> authors = authorQueryService.getAuthorsPage(authorFilterRequest);
		model.addAttribute("authors", authors);
		model.addAttribute("pageSize", authorFilterRequest.getPageSize());
		return "authors/all-authors.html";
	}
	
	@GetMapping(path="/{authorId:\\d+}")
	public String getAuthorPage(
			@PathVariable Long authorId,
			Model model) {
		AuthorDto author = authorQueryService.getAuthor(authorId);
		bookQueryService.getAuthorsHighestRatedBook(authorId).ifPresent(dto -> model.addAttribute("highRatedBook", dto));
		
		
		model.addAttribute("author", author);
		return "authors/author.html";
	}
	
}
