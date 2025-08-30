package shop.mapping.mapper.author;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.dto.author.ShortAuthorDto;
import shop.mapping.mappers.author.AuthorMapper;
import shop.persistence.entities.Author;

public class AuthorMapperTest {

	private AuthorMapper authorMapper = new AuthorMapper();
	
	@Test
	void mapForCreate_shouldMapCreateDtoToEntity() {
		CreateEditAuthorDto source = new CreateEditAuthorDto();
		source.setId(null);
		source.setFirstName("Leo");
		source.setLastName("Tolstoy");
		source.setBio("Biography");
		
		Author result = authorMapper.mapForCreate(source);
		
		assertThat(result.getId()).isNull();
		assertThat(result.getFirstName()).isEqualTo("Leo");
		assertThat(result.getLastName()).isEqualTo("Tolstoy");
		assertThat(result.getBio()).isEqualTo("Biography");
	}
	
	@Test
	void mapForUpdate_shouldMapFieldsButKeepIdUnchanged() {
		CreateEditAuthorDto source = new CreateEditAuthorDto();
		source.setId(1L);
		source.setFirstName("Leo");
		source.setLastName("Tolstoy");
		source.setBio("Biography");
		
		Author destination = new Author();
		destination.setId(2L);
		destination.setFirstName("LEO");
		destination.setLastName("TOLSTOY");
		destination.setBio("BIO");
		
		authorMapper.mapForUpdate(source, destination);
		
		assertThat(destination.getId()).isEqualTo(2L);
		assertThat(destination.getFirstName()).isEqualTo("Leo");
		assertThat(destination.getLastName()).isEqualTo("Tolstoy");
		assertThat(destination.getBio()).isEqualTo("Biography");
	}
	
	@Test
	void toShortDto_shouldMapEntityToShortDto() {
		Author entity = new Author();
		entity.setId(1L);
		entity.setFirstName("Leo");
		entity.setLastName("Tolstoy");
		
		ShortAuthorDto result = authorMapper.toShortDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getName()).isEqualTo("Leo Tolstoy");
	}
	
	@Test
	void toAuthorDto_shouldMapEntityToAuthorDto() {
		Author entity = new Author();
		entity.setId(1L);
		entity.setFirstName("Leo");
		entity.setLastName("Tolstoy");
		entity.setBio("Biography");
		
		AuthorDto result = authorMapper.toAuthorDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getName()).isEqualTo("Leo Tolstoy");
		assertThat(result.getBio()).isEqualTo("Biography");
	}
	
	@Test
	void toCreateEditAuthorDto_shouldMapEntityToCreateEditAuthorDto() {
		Author entity = new Author();
		entity.setId(1L);
		entity.setFirstName("Leo");
		entity.setLastName("Tolstoy");
		entity.setBio("Biography");
		
		CreateEditAuthorDto result = authorMapper.toCreateEditAuthorDto(entity);
		
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getFirstName()).isEqualTo("Leo");
		assertThat(result.getLastName()).isEqualTo("Tolstoy");
		assertThat(result.getBio()).isEqualTo("Biography");
	}
}
