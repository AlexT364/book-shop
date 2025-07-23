package shop.controllers.auth.passwords;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.password.NewPasswordDto;
import shop.exceptions.security.InvalidTokenException;
import shop.services.password.PasswordService;

@Controller
@RequestMapping(path = "/password")
@RequiredArgsConstructor
public class PasswordsController {

	private final PasswordService passwordService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping(path="/forgot")
	public String forgotPasswordPage() {
		return "auth-reg/passwords/forgot";
	}
	
	@PostMapping(path = "/forgot")
	public String handleForgotPassword(String email, RedirectAttributes redirectAttributes) {
		passwordService.sendPasswordResetLink(email);
		redirectAttributes.addFlashAttribute("message", "If an account with provided email exists, a password reset link has been sent.");
		return "redirect:/password/result";
	}
	
	@GetMapping(path="/reset")
	public String passwordResetForm(
			@RequestParam(name = "token") String passwordResetToken, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			passwordService.verifyToken(passwordResetToken);
			NewPasswordDto dto = new NewPasswordDto();
			dto.setToken(passwordResetToken);
			model.addAttribute("newPasswordRequest", dto);
			return "auth-reg/passwords/reset";
		}catch(InvalidTokenException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/password/forgot";
		}
	}
	
	@PostMapping(path = "/reset")
	public String handlePasswordReset(
			@Valid NewPasswordDto newPasswordDto, 
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		if(!newPasswordDto.passwordsAreEqual()) {
			bindingResult.rejectValue("password", "password.doNotMatch", "Passwords do not match.");
		}
		if(bindingResult.hasErrors()) {
			return "auth-reg/password/reset";
		}
		try {
			passwordService.updatePassword(newPasswordDto);
			model.addAttribute("message", "Password was successfully updated.");
		} catch (InvalidTokenException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/password/result";
	}
	
	@GetMapping(path="/result")
	public String forgotPasswordResultPage(Model model) {
		if(!model.containsAttribute("message")) {
			return "redirect:/";
		}
		return "auth-reg/passwords/result";
	}
}
