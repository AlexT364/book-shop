package shop.controllers.contact;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.contact.ContactMessageDto;
import shop.dto.contact.InboxMessagesFilterDto;
import shop.dto.contact.ReplyFormDto;
import shop.dto.contact.ShortContactMessageDto;
import shop.services.contact.ContactService;

@Controller
@RequestMapping(path="/admin/contacts/inbox")
@RequiredArgsConstructor
public class AdminContactController {

	private final ContactService contactService;

	@GetMapping
	public String adminContactsInboxPage(@ModelAttribute(name="inboxFilter") InboxMessagesFilterDto inboxFilterRequest, Model model) {
		inboxFilterRequest.normalizePagination();
		
		Page<ShortContactMessageDto> messages = contactService.getMessagesPaged(inboxFilterRequest);
		model.addAttribute("messages", messages);
		model.addAttribute("pageSize", inboxFilterRequest.getPageSize());
		return "contacts/contacts-inbox.html";
	}
	
	@GetMapping(path="/{messageId:\\d+}")
	public String adminContactMessagePage(
			@PathVariable Long messageId, 
			@ModelAttribute(name="reply") ReplyFormDto replyFormDto,
			Model model) {
		ContactMessageDto message = contactService.getMessage(messageId);
		model.addAttribute("message", message);
		
		return "contacts/contacts-reply.html";
	}
	
	@PostMapping(path="/{messageId:\\d+}")
	public String sendReply(
			@PathVariable Long messageId, 
			@ModelAttribute(name = "reply") @Valid ReplyFormDto replyFormDto, 
			BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			ContactMessageDto message = contactService.getMessage(messageId);
			model.addAttribute("message", message);
			return "contacts/contacts-reply.html";
		}
		
		contactService.sendReply(messageId, replyFormDto);
		
		return "redirect:/";
	}
}
