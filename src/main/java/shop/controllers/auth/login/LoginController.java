package shop.controllers.auth.login;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.security.SecurityUser;

@Controller
@RequestMapping(path = "/login")
public class LoginController {
	
	@GetMapping
	public String loginPage(
			@RequestParam(required = false) String error,
			@AuthenticationPrincipal SecurityUser user,
			Model model) {
		if("bad_credentials".equals(error)) {
			model.addAttribute("errorMessage", "Incorrect username or password.");
		}
		if(user != null) {
			model.addAttribute("message", "You are already logged in.");
		}
		return "auth-reg/login.html";
	}
}
