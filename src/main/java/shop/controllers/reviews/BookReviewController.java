package shop.controllers.reviews;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;
import shop.dto.reviews.BookReviewDto;
import shop.security.SecurityUser;
import shop.services.book.BookQueryService;
import shop.services.reviews.BookReviewService;

@Controller
@RequestMapping(path = "/book/{bookId:\\d+}/reviews")
@RequiredArgsConstructor
public class BookReviewController {

	private final BookQueryService bookQueryService;
	private final BookReviewService reviewService;

	@GetMapping
	public String bookReviewsPage(
	        @PathVariable Long bookId, 
	        Model model,
	        @RequestParam(defaultValue = "0") int pageNumber,
	        @RequestParam(defaultValue = "6") int pageSize,
	        @AuthenticationPrincipal SecurityUser securityUser) {
		
	    BookDto book;
	    if(securityUser != null) {
	    	String username = securityUser.getUsername();
	    	reviewService.getUsersReview(bookId, username)
	    		.ifPresent(review -> model.addAttribute("userReview", review));
	    	book = bookQueryService.getBookById(bookId, username);
	    }else {
	    	book = bookQueryService.getBookById(bookId);
	    }

	    
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("addedAt").descending());
	    Page<BookReviewDto> bookReviews = reviewService.getBookReviews(bookId, pageable);

	    model.addAttribute("reviews", bookReviews);
	    model.addAttribute("book", book);

	    return "books/book-reviews.html";
	}
}




















