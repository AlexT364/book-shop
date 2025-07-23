package shop.controllers.reviews;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.services.reviews.BookReviewService;

@Controller
@RequestMapping(path="/books/{bookId:\\d+}/reviews/admin/delete")
@RequiredArgsConstructor
public class BookReviewAdminActionController {
	
	private final BookReviewService reviewService;
	
	@DeleteMapping
	public String deleteUserReview(
			@RequestParam(name = "username") String authorUsername,
			@PathVariable long bookId) {
		reviewService.deleteReview(bookId, authorUsername);
		return "redirect:/books/{bookId:\\d+}/reviews";
	}
}
