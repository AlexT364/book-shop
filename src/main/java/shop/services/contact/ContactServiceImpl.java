package shop.services.contact;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.contact.ContactFormDto;
import shop.dto.contact.ContactMessageDto;
import shop.dto.contact.InboxMessagesFilterDto;
import shop.dto.contact.InboxMessagesFilterDto.DateOrder;
import shop.dto.contact.ReplyFormDto;
import shop.dto.contact.ShortContactMessageDto;
import shop.exceptions.contact.MessageNotFoundException;
import shop.mapping.mappers.ContactMessageMapper;
import shop.messaging.email.consumer.EmailContactReplyReceiver;
import shop.messaging.email.dto.EmailContactReplyPayload;
import shop.messaging.email.producer.EmailContactReplySender;
import shop.persistence.entities.ContactMessage;
import shop.persistence.repositories.contact.ContactMessageRepository;
import shop.services.email.EmailService;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService{
	
	private final EmailService emailService;
	private final ContactMessageRepository contactMessagesRepository;
	private final ContactMessageMapper contactMessageMapper;
	private final EmailContactReplySender contactReplySender;
	
	@Override
	public Page<ShortContactMessageDto> getMessagesPaged(InboxMessagesFilterDto inboxFilterRequest) {
		int pageNumber = inboxFilterRequest.getPageNumber();
		int pageSize = inboxFilterRequest.getPageSize();
		
		Sort sort = Sort.by(inboxFilterRequest.getDateOrder() == DateOrder.NEW_FIRST ? Sort.Direction.DESC : Sort.Direction.ASC, "addedAt");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<ShortContactMessageDto> messagesPage = contactMessagesRepository.findCriteriaMessages(pageable, inboxFilterRequest);

		return messagesPage;
	}
	
	@Override
	@Transactional
	public ContactMessageDto getMessage(Long messageId) {
		ContactMessage contactMessage = contactMessagesRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException("Message not found."));
		if(!contactMessage.isViewed()) {
			contactMessage.setViewed(true);
		}
		ContactMessageDto dto = contactMessageMapper.toContactMessageDto(contactMessage);
		return dto;
	}
	
	@Override
	@Transactional
	public void saveMessage(ContactFormDto contactForm) {
		
		ContactMessage messageToSave = contactMessageMapper.toEntity(contactForm);
		messageToSave.setAddedAt(LocalDateTime.now());
		messageToSave.setViewed(false);
		
		contactMessagesRepository.save(messageToSave);
	}

	@Override
	public void sendReply(long messageId, ReplyFormDto replyFormDto) {
		ContactMessage contactMessage = contactMessagesRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException("Message not found."));
		
		EmailContactReplyPayload replyEvent = new EmailContactReplyPayload();
		replyEvent.setToEmail(contactMessage.getEmail());
		replyEvent.setSubject(replyFormDto.getSubject());
		replyEvent.setMessage(replyFormDto.getMessage());
		
		contactReplySender.sendReplyMessage(replyEvent);
		
	}
	
}
