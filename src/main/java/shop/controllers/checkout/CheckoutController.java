package shop.controllers.checkout;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.CheckoutFormDto;
import shop.dto.cart.CartItemDto;
import shop.security.SecurityUser;
import shop.services.cart.CartService;
import shop.services.order.OrderService;

@Controller
@RequestMapping(path = "/checkout")
@RequiredArgsConstructor
public class CheckoutController {

	private final CartService cartService;
	private final ModelMapper modelMapper;
	private final OrderService orderService;

	@ModelAttribute(name="cart")
	public void checkoutData(Model model, @AuthenticationPrincipal SecurityUser user) {
		CheckoutFormDto orderForm = new CheckoutFormDto();
		List<CartItemDto> cart = cartService.getBooksInCart(user.getUsername())
				.stream()
				.map(cartBook -> modelMapper.map(cartBook, CartItemDto.class))
				.toList();
		BigDecimal totalPrice = cart
				.stream()
				.map(cartBookDto -> cartBookDto.getSubtotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("checkoutForm", orderForm);
		model.addAttribute("cart", cart);
	}
	
	@GetMapping
	public String checkoutPage(Model model) {
		
		return "checkout.html";
	}
	
	@PostMapping
	public String placeOrder(
			@ModelAttribute(name="checkoutForm") @Valid CheckoutFormDto checkoutForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal SecurityUser securityUser,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult);
			model.addAttribute("checkoutForm", checkoutForm);
			return "checkout.html";
		}
		
		orderService.createOrder(checkoutForm, securityUser.getUsername());
		return "redirect:/";
	}
}







































