package shop.mapping.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.contact.ContactFormDto;
import shop.dto.contact.ContactMessageDto;
import shop.persistence.entities.ContactMessage;

@Component
@RequiredArgsConstructor
public class ContactMessageMapper {
	
	private final ModelMapper modelMapper;
	
	public ContactMessage toEntity(ContactFormDto dto) {
		return modelMapper.map(dto, ContactMessage.class);
	}
	
	public ContactMessageDto toContactMessageDto(ContactMessage entity) {
		return modelMapper.map(entity, ContactMessageDto.class);
	}
	
}
