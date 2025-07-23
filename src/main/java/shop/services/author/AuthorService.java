package shop.services.author;

import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;

public interface AuthorService {
	
	public AuthorDto addAuthor(CreateEditAuthorDto authorDto, byte[] authorImage);
	public AuthorDto updateAuthor(long authorId, CreateEditAuthorDto authorDto, byte[] authorImage);
	public void deleteAuthor(Long authorId);
}
