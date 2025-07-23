package shop.controllers.auth.registration;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.RegisterUserRequest;
import shop.services.registration.RegistrationService;

@Controller
@RequestMapping(path = "/registration")
@RequiredArgsConstructor
public class RegistrationController extends UriAwareController {

	private final RegistrationService registrationService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		PropertyEditorSupport trimmingAndLowercaseEditor = new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				if(text == null) {
					setValue(null);
				}else {
					setValue(text.trim().toLowerCase());
				}
			}
		};
		
		dataBinder.registerCustomEditor(String.class, "username", trimmingAndLowercaseEditor);
		dataBinder.registerCustomEditor(String.class, "email", trimmingAndLowercaseEditor);
		
	}
	
	@GetMapping
	public String registrationPage(Model model) {
		model.addAttribute("registrationRequest", new RegisterUserRequest());
		return "auth-reg/registration.html";
	}

	@PostMapping
	public String registerUser(@ModelAttribute(name = "registrationRequest") @Valid RegisterUserRequest registrationRequest,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			if (!registrationRequest.passwordsAreEqual()) {
				bindingResult.addError(new FieldError("registrationRequest", "passwordConfirm", "Passwords do not match."));
			}
			model.addAttribute("errors", bindingResult);
			return "auth-reg/registration.html";
		}

		registrationService.registerUser(registrationRequest);
		model.addAttribute("message", "You have successfully registered.");
		return "auth-reg/registration-success.html";
	}

	@GetMapping(path = "/confirm")
	public String confirmRegistration(@RequestParam String token, Model model) {
		registrationService.confirmUser(token);
		return "auth-reg/confirmation-success.html";
	}

	@GetMapping(path = "/resend-confirmation")
	public String resendVerificationPage() {
		return "auth-reg/confirmation-resend-page.html";
	}
	
}

















