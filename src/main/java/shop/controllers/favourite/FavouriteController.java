package shop.controllers.favourite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import shop.dto.ShopRequestDto;
import shop.dto.author.ShortAuthorDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.security.SecurityUser;
import shop.services.favourite.FavouriteService;

@Controller
@RequestMapping(path="/favourite")
@RequiredArgsConstructor
public class FavouriteController {
	
	private final FavouriteService favouriteService;
	
	@GetMapping
	public String favouritesPage(
			@AuthenticationPrincipal SecurityUser securityUser,
			@ModelAttribute(name = "filters") ShopRequestDto bookFilterRequest, 
			Model model) {
		bookFilterRequest.normalizePricesAndPaging();
		
		Page<ShortBookDto> favouriteBooks = favouriteService.getFavouriteBooks(bookFilterRequest, securityUser.getUsername());

		if (bookFilterRequest.getMaxPrice() == 0) {
			BigDecimal maxPrice = favouriteService.getMaxPriceOfFavourites(bookFilterRequest, securityUser.getUsername())
					.setScale(0, RoundingMode.UP);
			
			bookFilterRequest.setMaxPrice(maxPrice.intValue());
		}
		
		List<GenreDto> genres = favouriteService.getDistinctGenresInUserFavourites(securityUser.getUsername());
		List<ShortAuthorDto> authors = favouriteService.getDistinctAuthorsInUserFavourites(securityUser.getUsername());
		
		model.addAttribute("books", favouriteBooks);
		model.addAttribute("pageSize", bookFilterRequest.getPageSize());
		model.addAttribute("currentPage", favouriteBooks.getNumber());
		model.addAttribute("genres", genres);
		model.addAttribute("authors", authors);
		
		return "favourite.html";
	}

	@PostMapping("/{bookId:\\d+}")
	public String addToFavourite(@PathVariable Long bookId, @AuthenticationPrincipal SecurityUser securityUser) {
		favouriteService.addBook(securityUser.getUsername(), bookId);
		
		return "redirect:/favourite".formatted(bookId);
	}
		
	@DeleteMapping(path = "/{bookId:\\d+}")
	public String deleteFromFavourite(@PathVariable Long bookId, @AuthenticationPrincipal SecurityUser securityUser) {
		favouriteService.deleteBook(bookId, securityUser.getUsername());
		
		return "redirect:/favourite";
	}
}






























