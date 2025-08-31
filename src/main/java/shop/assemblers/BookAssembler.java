package shop.assemblers;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.book.CreateEditBookDto;
import shop.mapping.mappers.book.BookMapper;
import shop.persistence.entities.Book;
import shop.persistence.repositories.AuthorRepository;
import shop.persistence.repositories.GenresRepository;

@Component
@RequiredArgsConstructor
public class BookAssembler {
	
	private final AuthorRepository authorRepository;
	private final GenresRepository genreRepository;
	private final BookMapper bookMapper;
	
	public Book createBookFromDto(CreateEditBookDto dto) {
		Book entity = bookMapper.mapForCreate(dto);
		entity.setUnitsReserved(0);
		entity.setAuthors(authorRepository.findAuthorsByIds(dto.getAuthors()));
		entity.setGenres(genreRepository.findGenresByIds(dto.getGenres()));
		return entity;
	}
	
	public void updateBookFromDto(CreateEditBookDto dto, Book entity) {
		bookMapper.mapForUpdate(dto, entity);
		entity.setAuthors(authorRepository.findAuthorsByIds(dto.getAuthors()));
		entity.setGenres(genreRepository.findGenresByIds(dto.getGenres()));
	}
}
