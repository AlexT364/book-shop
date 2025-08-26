package shop.controllers.cart;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.cart.CartItemDto;
import shop.exceptions.NotEnoughItemsException;
import shop.exceptions.user.UserNotFoundException;
import shop.security.SecurityUser;
import shop.services.cart.CartService;

@Controller
@RequestMapping(path = "/cart")
@SessionAttributes("order")
@RequiredArgsConstructor
public class CartController extends UriAwareController{
	
	private final CartService cartService;
	
	@GetMapping
	public String cartPage(Model model, @AuthenticationPrincipal SecurityUser userDetails) {
		if(userDetails == null) {
			return "redirect:/login";
		}
		List<CartItemDto> booksDtos = cartService.getAllBooksInCart(userDetails.getUsername());
		BigDecimal totalPrice = booksDtos.stream().map(b -> b.getSubtotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
		model.addAttribute("cart", booksDtos);
		model.addAttribute("totalPrice", totalPrice);
		return "cart.html";
	}
	
	@PostMapping
	public String addBookToCart(
			long bookId,
			int quantity,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		if(userDetails == null) {
			return "redirect:/login";
		}
		try {
		cartService.addBook(bookId, quantity, userDetails.getUsername());
		}catch(NotEnoughItemsException | UserNotFoundException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error.html";
		}
		return "redirect:/shop";
	}
	
	@DeleteMapping(path = "/{bookId}")
	public String deleteBookFromCart(
			@PathVariable long bookId,
			@AuthenticationPrincipal UserDetails userDetails) {
			if(userDetails == null) {
				return "redirect:/login";
			}
			cartService.removeBook(bookId, userDetails.getUsername());
		
		return "redirect:/cart";
	}
}




























