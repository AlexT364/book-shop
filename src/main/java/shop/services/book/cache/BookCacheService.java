package shop.services.book.cache;

import java.util.Optional;

import shop.dto.book.BookDto;

public interface BookCacheService {
	
	Optional<BookDto> getCachedBook(long bookId);
	
	void cacheBook(BookDto bookDto);
	
	void evictBook(long bookId);
	
	Optional<Double> getCachedScore(long bookId);
	
	void cacheScore(long bookId, Double score);
	
	void evictScore(long bookId);
	
}
