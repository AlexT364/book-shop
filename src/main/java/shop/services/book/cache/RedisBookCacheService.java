package shop.services.book.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import shop.dto.book.BookDto;

@Service
public class RedisBookCacheService implements BookCacheService {

	private final RedisTemplate<String, BookDto> bookDtoRedisTemplate;
	private final RedisTemplate<String, Double> doubleRedisTemplate;
	private static final String BOOKS_KEY_PREFIX = "books::";
	private static final Duration BOOK_TTL = Duration.ofHours(1);
	private static final Duration SCORE_TTL = Duration.ofMinutes(15);
	
	public RedisBookCacheService(
			@Qualifier("bookDtoRedisTemplate") RedisTemplate<String, BookDto> bookDtoRedisTemplate,
			@Qualifier("doubleRedisTemplate") RedisTemplate<String, Double> doubleRedisTemplate
			) {
		this.bookDtoRedisTemplate = bookDtoRedisTemplate;
		this.doubleRedisTemplate = doubleRedisTemplate;
	}
	
	@Override
	public Optional<BookDto> getCachedBook(long bookId) {
		String key = BOOKS_KEY_PREFIX + bookId;
		BookDto dto = bookDtoRedisTemplate.opsForValue().get(key);
		return Optional.ofNullable(dto);
	}

	@Override
	public void cacheBook(BookDto bookDto) {
		String key = BOOKS_KEY_PREFIX + bookDto.getId();
		bookDtoRedisTemplate.opsForValue().set(key, bookDto, BOOK_TTL);
	}

	@Override
	public void evictBook(long bookId) {
		String key = BOOKS_KEY_PREFIX + bookId;
		bookDtoRedisTemplate.delete(key);
	}
	
	@Override
	public Optional<Double> getCachedScore(long bookId) {
		String key = BOOKS_KEY_PREFIX + bookId + ":score";
		Double score = doubleRedisTemplate.opsForValue().get(key);
		return Optional.ofNullable(score);
	}

	@Override
	public void cacheScore(long bookId, Double score) {
		String key = BOOKS_KEY_PREFIX + bookId + ":score";
		doubleRedisTemplate.opsForValue().set(key, score, SCORE_TTL);
	}

	@Override
	public void evictScore(long bookId) {
		String key = BOOKS_KEY_PREFIX + bookId + ":score";
		doubleRedisTemplate.delete(key);
	}
	
	

}
