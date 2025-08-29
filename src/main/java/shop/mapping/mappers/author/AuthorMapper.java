package shop.mapping.mappers.author;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.dto.author.ShortAuthorDto;
import shop.persistence.entities.Author;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
	
	public Author mapForCreate(CreateEditAuthorDto source) {
		Author entity = new Author();
		entity.setFirstName(source.getFirstName());
		entity.setLastName(source.getLastName());
		entity.setBio(source.getBio());
		return entity;
	}
	
	public void mapForUpdate(CreateEditAuthorDto source, Author destination) {
		
	}
	
	public ShortAuthorDto toShortDto(Author entity) {
		ShortAuthorDto dto = new ShortAuthorDto(entity.getId(), entity.getFirstName() + " " + entity.getLastName());
		return dto;
	}
	
	public AuthorDto toAuthorDto(Author entity) {
		AuthorDto dto = new AuthorDto();
		dto.setId(entity.getId());
		String fullName = entity.getFirstName() + " " + entity.getLastName();
		dto.setName(fullName);
		dto.setBio(entity.getBio());
		return dto;
	}

	public CreateEditAuthorDto toCreateEditAuthorDto(Author entity) {
		CreateEditAuthorDto dto = new CreateEditAuthorDto();
		
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setBio(entity.getBio());
		
		return dto;
	} 
}
