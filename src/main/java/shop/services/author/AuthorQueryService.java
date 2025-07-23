package shop.services.author;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shop.dto.author.AuthorDto;
import shop.dto.author.AuthorFilterRequestDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.dto.author.ShortAuthorDto;

public interface AuthorQueryService {

	//TODO: Возвращаются все Author
	public List<AuthorDto> getAllAuthors();

	public List<ShortAuthorDto> getAllAuthorsShort();

	public Page<ShortAuthorDto> getAuthorsPage(AuthorFilterRequestDto authorFilterRequest);

	public AuthorDto getAuthor(Long authorId);

	public CreateEditAuthorDto getAuthorByIdForEdit(Long authorId);
	
	public Page<ShortAuthorDto> getAuthors(Pageable pageable);
}
