package shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;
import shop.services.book.BookQueryService;

@Controller
@RequiredArgsConstructor
@SessionAttributes("order")
public class HomeController extends UriAwareController{
	
	private final BookQueryService bookQueryService;
	
	@ModelAttribute
	public void prepareModel(Model model) {
		
		model.addAttribute("latestBooks", bookQueryService.getLatestBooks());
		model.addAttribute("popularBooks", bookQueryService.getPopularBooks());
		model.addAttribute("highestRatedBooks", bookQueryService.getHighestRatedBooks());
	}
	
	@GetMapping(path = "/")
	public String getHomePage() {
		return "home.html";
	}

	
}


















