package shop.services.author;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.exceptions.author.AuthorNotFoundException;
import shop.mapping.mappers.AuthorMapper;
import shop.persistence.entities.Author;
import shop.persistence.repositories.AuthorRepository;
import shop.services.image.AuthorImageService;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
	
	@InjectMocks
	private AuthorServiceImpl authorService;
	@Mock
	private AuthorMapper authorMapper;
	@Mock
	private AuthorImageService authorImageService;
	@Mock
	private AuthorRepository authorRepository;
	
	@Test
	public void addAuthor_ShouldSaveAuthor() {
		CreateEditAuthorDto dto = new CreateEditAuthorDto();
		byte[] image = new byte[0];
		Author entity = new Author();
		entity.setId(1L);
		AuthorDto expected = new AuthorDto();
		
		when(authorMapper.mapForCreate(dto)).thenReturn(entity);
		when(authorRepository.save(entity)).thenReturn(entity);
		when(authorMapper.toAuthorDto(entity)).thenReturn(expected);
		
		AuthorDto result = authorService.addAuthor(dto, image);
		
		assertNotNull(result);
		assertSame(expected, result);
		
		verify(authorRepository).save(entity);
		verify(authorImageService).saveImage(entity.getId(), image);
	}
	
	@Test
	public void updateAuthor_ShouldThrowException_WhenAuthorNotExists() {
		long authorId = 1L;
		CreateEditAuthorDto dto = new CreateEditAuthorDto();
		byte[] authorImage = new byte[0];
		
		when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
		
		assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(authorId, dto, authorImage));
		
		verify(authorRepository).findById(authorId);
		verifyNoMoreInteractions(authorRepository);
		verifyNoInteractions(authorMapper, authorImageService);
	}
	
	@Test
	public void updateAuthor_ShouldUpdateAuthorAndSaveImage_WhenArgumentsAreValid() {
		long authorId = 1L;
		CreateEditAuthorDto dto = new CreateEditAuthorDto();
		byte[] authorImage = new byte[0];
		Author entity = new Author();
		entity.setId(authorId);
		AuthorDto expected = new AuthorDto();
		
		when(authorRepository.findById(authorId)).thenReturn(Optional.of(entity));
		when(authorRepository.save(entity)).thenReturn(entity);
		when(authorMapper.toAuthorDto(entity)).thenReturn(expected);
		
		AuthorDto result = authorService.updateAuthor(authorId, dto, authorImage);
		
		assertNotNull(result);
		assertSame(expected, result);
		
		verify(authorRepository).save(entity);
		verify(authorImageService).saveImage(entity.getId(), authorImage);
	}
	
	@Test
	public void deleteAuthor_ShouldDeleteAuthorAndImage() {
		long authorId = 1L;
		
		authorService.deleteAuthor(authorId);
		
		verify(authorRepository).deleteById(authorId);
		verify(authorImageService).deleteImage(authorId);
	}
}



















