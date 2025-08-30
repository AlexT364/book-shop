package shop.services.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import shop.dto.author.AuthorDto;
import shop.dto.author.AuthorFilterRequestDto;
import shop.dto.author.AuthorFilterRequestDto.AuthorOrder;
import shop.dto.author.CreateEditAuthorDto;
import shop.dto.author.ShortAuthorDto;
import shop.exceptions.author.AuthorNotFoundException;
import shop.mapping.mappers.author.AuthorMapper;
import shop.persistence.repositories.AuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorQueryServiceTest {
	
	@InjectMocks
	private AuthorQueryServiceImpl authorQueryService;
	@Mock
	private AuthorRepository authorRepository;
	@Mock
	private AuthorMapper authorMapper;
	
	@Test
	void getAuthorsPage_shouldReturnPage_whenOrderAsc() {
		AuthorFilterRequestDto request = new AuthorFilterRequestDto();
		request.setPageNumber(0);
		request.setPageSize(5);
		request.setLastName("Tolstoy");
		request.setAuthorOrder(AuthorOrder.LAST_NAME_ASC);
		
		Pageable expectedPageable = PageRequest.of(
				0, 
				5, 
				Sort.by(Direction.ASC, "lastName"));
		
		List<ShortAuthorDto> authors = List.of(new ShortAuthorDto(1L, "Leo Tolstoy"));
		Page<ShortAuthorDto> expectedPage = new PageImpl<>(authors, expectedPageable, 1);
		
		when(authorRepository.findAllAsShortDto("Tolstoy", expectedPageable)).thenReturn(expectedPage);
		
		Page<ShortAuthorDto> result = authorQueryService.getAuthorsPage(request);
		
		assertThat(result).isEqualTo(expectedPage);
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getName()).isEqualTo("Leo Tolstoy");
		
		verify(authorRepository, times(1)).findAllAsShortDto("Tolstoy", expectedPageable);
	}
	
	@Test
	void getAuthorsPage_shouldUseDescOrder_whenOrderIsNotAsc() {
		AuthorFilterRequestDto request = new AuthorFilterRequestDto();
		request.setPageNumber(0);
		request.setPageSize(5);
		request.setAuthorOrder(AuthorOrder.LAST_NAME_DESC);
		request.setLastName(null);
		
		Pageable expectedPageable = PageRequest.of(
				0, 
				5, 
				Sort.by(Direction.DESC, "lastName"));
		
		Page<ShortAuthorDto> emptyPage = Page.empty();
		
		when(authorRepository.findAllAsShortDto(null, expectedPageable)).thenReturn(emptyPage);
		
		Page<ShortAuthorDto> result = authorQueryService.getAuthorsPage(request);
		
		assertThat(result).isEmpty();
		verify(authorRepository, times(1)).findAllAsShortDto(null, expectedPageable);
		
	}
	
	@Test
	void getAuthor_shouldReturnDto_whenAuthorExists() {
		Long authorId = 1L;
		
		AuthorDto expectedDto = new AuthorDto();
		expectedDto.setId(1L);
		
		when(authorRepository.findDtoById(authorId)).thenReturn(Optional.of(expectedDto));
		
		AuthorDto result = authorQueryService.getAuthor(authorId);
		
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		
		verify(authorRepository).findDtoById(authorId);
		verifyNoMoreInteractions(authorRepository);
	}
	
	@Test
	void getAuthor_shouldThrowException_whenAuthorNotExists() {
		Long authorId = 1L;
		
		when(authorRepository.findDtoById(authorId)).thenReturn(Optional.empty());
		
		assertThrows(AuthorNotFoundException.class, () -> authorQueryService.getAuthor(authorId));
		
		verify(authorRepository, times(1)).findDtoById(authorId);
		verifyNoMoreInteractions(authorRepository);
	}
	
	
	@Test
	void getAuthorByIdForEdit_shouldReturnDto_whenAuthorExists() {
		Long authorId = 1L;
		
		CreateEditAuthorDto expectedDto = new CreateEditAuthorDto();
		expectedDto.setId(1L);
		
		when(authorRepository.findDtoByIdForEdit(authorId)).thenReturn(Optional.of(expectedDto));
		
		CreateEditAuthorDto result = authorQueryService.getAuthorByIdForEdit(authorId);
		
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		
		verify(authorRepository).findDtoByIdForEdit(authorId);
		verifyNoMoreInteractions(authorRepository);
	}
	
	@Test
	void getAuthorByIdForEdit_shouldThrowException_whenAuthorNotExists() {
		Long authorId = 1L;
		
		when(authorRepository.findDtoByIdForEdit(authorId)).thenReturn(Optional.empty());
		
		assertThrows(AuthorNotFoundException.class, () -> authorQueryService.getAuthorByIdForEdit(authorId));
		
		verify(authorRepository, times(1)).findDtoByIdForEdit(authorId);
		verifyNoMoreInteractions(authorRepository);
	}
}
