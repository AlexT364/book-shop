package shop.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

/**
 * As of right now, this abstract controller returns request URI, that is used to further stylize navigation links inside page header.
 * May be changed in the future.
 * Inherited by: ShopController, HomeController, ContactController, AuthorViewController, CartController, SalesController
 */
public abstract class UriAwareController {
	
	@ModelAttribute
	public void requestUri(HttpServletRequest req, Model model) {
		model.addAttribute("requestUri", req.getRequestURI());
	}
}
