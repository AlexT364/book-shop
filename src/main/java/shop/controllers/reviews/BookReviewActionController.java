package shop.controllers.reviews;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;
import shop.dto.reviews.BookReviewDto;
import shop.dto.reviews.CreateBookReviewDto;
import shop.security.SecurityUser;
import shop.services.book.BookQueryService;
import shop.services.reviews.BookReviewService;

@Controller
@RequestMapping(path="/book/{bookId:\\d+}/reviews")
@RequiredArgsConstructor
public class BookReviewActionController {
	
	private final BookReviewService reviewService;
	private final BookQueryService bookQueryService;
	
	@PostMapping
	public String postUserReview(
			@PathVariable long bookId,
			@Valid @ModelAttribute(name = "newReview") CreateBookReviewDto newBookReview,
			BindingResult bindingResult,
			Model model,
			@AuthenticationPrincipal SecurityUser securityUser
			) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult);
			this.prepareBookReviewsPageModel(model, bookId, 0, 0, securityUser);
			return "books/book-reviews.html";
		}
		
		reviewService.addNewReview(
				securityUser.getUsername(),
				bookId,
				newBookReview.getReview(),
				newBookReview.getScore()
				);
		
		return "redirect:/book/%d/reviews".formatted(bookId);
	}
	
	@DeleteMapping
	public String deleteUserReview(
			@PathVariable long bookId,
			@AuthenticationPrincipal SecurityUser securityUser
			) {
		reviewService.deleteReview(bookId, securityUser.getUsername());
		return "redirect:/book/%d/reviews".formatted(bookId);
	}
	
	private void prepareBookReviewsPageModel(
			Model model,
			long bookId,
			int pageNumber,
			int pageSize,
			SecurityUser securityUser
			) {
		
		BookDto book = securityUser == null ? 
				bookQueryService.getBookById(bookId) :
				bookQueryService.getBookById(bookId, securityUser.getUsername());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("addedAt").descending());
		
		Page<BookReviewDto> bookReviews = reviewService.getBookReviews(bookId, pageable);
		model.addAttribute("reviews", bookReviews);
		model.addAttribute("book", book);
	}
}
















