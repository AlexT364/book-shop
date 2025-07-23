package shop.exceptions.handlers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import shop.exceptions.OrderNotFoundException;
import shop.exceptions.security.TokenExpiredException;
import shop.exceptions.user.UserAlreadyExistsException;
import shop.exceptions.user.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(OrderNotFoundException.class)
	public String handleOrderNotFound(
			OrderNotFoundException ex, 
			HttpServletResponse response,
			Model model) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		model.addAttribute("errorCode", response.getStatus());
		model.addAttribute("errorMessage", ex.getMessage());
		return "error.html";
	};
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public String handleUserAlreadyExists(
			UserAlreadyExistsException ex,
			HttpServletResponse response,
			Model model
			) {
		response.setStatus(HttpServletResponse.SC_CONFLICT);
		model.addAttribute("errorCode", response.getStatus());
		model.addAttribute("errorMessage", ex.getMessage());
		return "error.html";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFound(
			UserNotFoundException ex,
			HttpServletResponse response,
			Model model
			) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		model.addAttribute("errorCode", response.getStatus());
		model.addAttribute("errorMessage", ex.getMessage());
		return "error.html";
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public String handleTokenExpiredException(
			TokenExpiredException ex,
			HttpServletResponse response,
			Model model
			) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		model.addAttribute("errorCode", response.getStatus());
		model.addAttribute("errorMessage", ex.getMessage());
		return "auth-reg/confirmation-resend-page.html";
	}
}
