package shop.services.author;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.author.AuthorDto;
import shop.dto.author.CreateEditAuthorDto;
import shop.exceptions.author.AuthorNotFoundException;
import shop.mapping.mappers.author.AuthorMapper;
import shop.persistence.entities.Author;
import shop.persistence.repositories.AuthorRepository;
import shop.services.image.AuthorImageService;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;
	private final AuthorMapper authorMapper;
	private final AuthorImageService authorImageService;
	
	@Override
	@Transactional
	@CachePut(cacheNames = "authors", key = "#result.id")
	public AuthorDto addAuthor(CreateEditAuthorDto authorDto, byte[] authorImage) {
		Author entity = this.createAuthorAndSaveImage(authorDto, authorImage);
		return authorMapper.toAuthorDto(entity);
	}
	
	@Override
	@Transactional
	@CachePut(cacheNames = "authors", key = "#result.id")
	public AuthorDto updateAuthor(long authorId, CreateEditAuthorDto authorDto, byte[] authorImage) {
		Author entity = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException());
		
		this.updateAuthorAndSaveImage(authorDto, entity, authorImage);
		
		return authorMapper.toAuthorDto(entity);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "authors", key = "#authorId")
	public void deleteAuthor(Long authorId) {
		authorRepository.deleteById(authorId);
		authorImageService.deleteImage(authorId);
	}
	
	private Author createAuthorAndSaveImage(CreateEditAuthorDto dto, byte[] authorImage) {
		Author entity = authorMapper.mapForCreate(dto);
		entity = authorRepository.save(entity);
		authorImageService.saveImage(entity.getId(), authorImage);
		return entity;
	}
	
	private void updateAuthorAndSaveImage(CreateEditAuthorDto dto, Author entity, byte[] authorImage) {
		authorMapper.mapForUpdate(dto, entity);
		entity = authorRepository.save(entity);
		authorImageService.saveImage(entity.getId(), authorImage);
	}
}













