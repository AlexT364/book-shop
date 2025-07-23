package shop.controllers.book;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;
import shop.security.SecurityUser;
import shop.services.book.BookQueryService;

@Controller
@RequestMapping(path = "/book/{bookId:\\d+}")
@RequiredArgsConstructor
public class BookViewController {

	private final BookQueryService bookQueryService;
	
	@ModelAttribute(name = "book")
	public BookDto addBookDtoToModel(
			@PathVariable long bookId,
			@AuthenticationPrincipal SecurityUser securityUser) {
		BookDto bookDto = securityUser == null ? 
				bookQueryService.getBookById(bookId) : 
				bookQueryService.getBookById(bookId, securityUser.getUsername());
		System.out.println(bookDto);
		return bookDto;
	}

	@GetMapping
	public String getBookPage() {
		return "books/book.html";
	}
}
