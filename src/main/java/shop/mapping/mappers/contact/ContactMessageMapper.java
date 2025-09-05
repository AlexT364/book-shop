package shop.mapping.mappers.contact;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.contact.ContactFormDto;
import shop.dto.contact.ContactMessageDto;
import shop.persistence.entities.ContactMessage;

@Component
@RequiredArgsConstructor
public class ContactMessageMapper {
	
	public ContactMessage toContactMessage(ContactFormDto dto) {
		ContactMessage entity = new ContactMessage();
		
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setSubject(dto.getSubject());
		entity.setMessage(dto.getMessage());
		
		return entity;
	}
	
	public ContactMessageDto toContactMessageDto(ContactMessage entity) {
		ContactMessageDto dto = new ContactMessageDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setSubject(entity.getSubject());
		dto.setMessage(entity.getMessage());
		
		return dto;
	}
	
}
