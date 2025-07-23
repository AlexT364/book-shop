package shop.services.contact;

import org.springframework.data.domain.Page;

import shop.dto.contact.ContactFormDto;
import shop.dto.contact.ContactMessageDto;
import shop.dto.contact.InboxMessagesFilterDto;
import shop.dto.contact.ReplyFormDto;
import shop.dto.contact.ShortContactMessageDto;

public interface ContactService {
	
	public Page<ShortContactMessageDto> getMessagesPaged(InboxMessagesFilterDto inboxFilterRequest);
	public ContactMessageDto getMessage(Long messageId);
	public void saveMessage(ContactFormDto contactFormDto);
	public void sendReply(long messageId, ReplyFormDto replyFormDto);

}
