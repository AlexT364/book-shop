package shop.mapping.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.reviews.BookReviewDto;
import shop.dto.reviews.CreateBookReviewDto;
import shop.persistence.entities.BookReview;

@Component
@RequiredArgsConstructor
public class BookReviewMapper {
	
	private final ModelMapper modelMapper;
	
	public BookReviewDto mapToDto(BookReview bookReview) {
		return modelMapper.map(bookReview, BookReviewDto.class);
	}
	
	
	public BookReview mapToEntity(CreateBookReviewDto dto) {
		return modelMapper.map(dto, BookReview.class);
	}
}
