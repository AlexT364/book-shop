package shop.mapping.converters.contact;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.contact.ContactFormDto;
import shop.persistence.entities.ContactMessage;

public class ContactFormToContactMessageConverter implements Converter<ContactFormDto, ContactMessage> {

	@Override
	public ContactMessage convert(MappingContext<ContactFormDto, ContactMessage> context) {
		ContactFormDto dto = context.getSource();
		ContactMessage contactMessage = new ContactMessage();
		
		contactMessage.setName(dto.getName());
		contactMessage.setEmail(dto.getEmail());
		contactMessage.setPhone(dto.getPhone());
		contactMessage.setSubject(dto.getSubject());
		contactMessage.setMessage(dto.getMessage());
		
		return contactMessage;
	}

}
