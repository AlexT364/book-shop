package shop.persistence.repositories.contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.contact.InboxMessagesFilterDto;
import shop.dto.contact.ShortContactMessageDto;

public interface CustomMessageRepository{
	
	Page<ShortContactMessageDto> findCriteriaMessages(Pageable pageable, InboxMessagesFilterDto inboxFilterRequest);

}
