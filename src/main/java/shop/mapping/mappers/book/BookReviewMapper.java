package shop.mapping.mappers.book;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.reviews.BookReviewDto;
import shop.persistence.entities.BookReview;

@Component
@RequiredArgsConstructor
public class BookReviewMapper {
	
	public BookReviewDto mapToDto(BookReview entity) {
		BookReviewDto dto = new BookReviewDto();
		dto.setUsername(entity.getUser().getUsername());
		dto.setAddedAt(entity.getAddedAt());
		dto.setReview(entity.getReview());
		dto.setScore(entity.getScore());
		
		return dto;
	}
}
