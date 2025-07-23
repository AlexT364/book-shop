package shop.controllers.contact;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.contact.ContactFormDto;
import shop.services.contact.ContactService;

@Controller
@RequestMapping(path = "/contacts")
@RequiredArgsConstructor
public class ContactController extends UriAwareController{
	
	private final ContactService contactService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String contactsPage(@ModelAttribute(name="contactForm") ContactFormDto contactForm) {
		return "contacts/contacts.html";
	}
	
	@PostMapping
	public String submitMessage(
			@ModelAttribute(name="contactForm") @Valid ContactFormDto contactForm,
			BindingResult bindingResult,
			Model model) {
		if(bindingResult.hasErrors()) {
			return "contacts/contacts.html";
		}
		
		contactService.saveMessage(contactForm);
		
		return "contacts/contacts-success.html";
	}
}
