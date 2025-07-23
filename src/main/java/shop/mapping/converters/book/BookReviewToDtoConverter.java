package shop.mapping.converters.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.reviews.BookReviewDto;
import shop.persistence.entities.BookReview;

public class BookReviewToDtoConverter implements Converter<BookReview, BookReviewDto>{

	@Override
	public BookReviewDto convert(MappingContext<BookReview, BookReviewDto> context) {
		BookReview entity = context.getSource();
		BookReviewDto dto = new BookReviewDto();
		dto.setUsername(entity.getUser().getUsername());
		dto.setAddedAt(entity.getAddedAt());
		dto.setReview(entity.getReview());
		dto.setScore(entity.getScore());
		return dto;
	}
	
	
}
