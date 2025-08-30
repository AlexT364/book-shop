package shop.services.author;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.author.AuthorFilterRequestDto;
import shop.dto.author.AuthorFilterRequestDto.AuthorOrder;
import shop.dto.author.CreateEditAuthorDto;
import shop.dto.author.ShortAuthorDto;
import shop.exceptions.author.AuthorNotFoundException;
import shop.mapping.mappers.author.AuthorMapper;
import shop.persistence.entities.Author;
import shop.persistence.repositories.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorQueryServiceImpl implements AuthorQueryService {

	private final AuthorRepository authorRepository;
	private final AuthorMapper authorMapper;
	
	@Override
	public Page<ShortAuthorDto> getAuthors(Pageable pageable) {
		return authorRepository.findAll(pageable).map(authorMapper::toShortDto);
	}

	@Override
	public List<AuthorDto> getAllAuthors() {
		return authorRepository.findAll().stream().map(author -> authorMapper.toAuthorDto(author)).toList();
	}
	
	@Override
	public List<ShortAuthorDto> getAllAuthorsShort() {
		return authorRepository.findAllSortByName();
	}
	
	@Override
	public Page<ShortAuthorDto> getAuthorsPage(AuthorFilterRequestDto authorFilterRequest) {
		int pageNumber = authorFilterRequest.getPageNumber();
		int pageSize = authorFilterRequest.getPageSize();
		
		Sort sort = Sort.by(authorFilterRequest.getAuthorOrder() == AuthorOrder.LAST_NAME_ASC ? Sort.Direction.ASC : Sort.Direction.DESC, "lastName");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return authorRepository.findAllAsShortDto(authorFilterRequest.getLastName(), pageable);
	}
	
	@Override
	@Cacheable(cacheNames = "authors", key = "#authorId")
	public AuthorDto getAuthor(Long authorId) {
		AuthorDto dto = authorRepository.findDtoById(authorId)
				.orElseThrow(() -> new AuthorNotFoundException("Author with id = %d not found".formatted(authorId)));
		return dto;
	}

	@Override
	public CreateEditAuthorDto getAuthorByIdForEdit(Long authorId) {
		CreateEditAuthorDto dto = authorRepository.findDtoByIdForEdit(authorId)
				.orElseThrow(() -> new AuthorNotFoundException("Author with id = %d not found".formatted(authorId)));;
		return dto;
	}
}
