package shop.mapping.mappers.discount;

import org.springframework.stereotype.Component;

import shop.dto.discount.DiscountDto;
import shop.persistence.entities.Discount;

@Component
public class DiscountMapper {
	
	public DiscountDto toDiscountDto(Discount entity) {
		DiscountDto dto = new DiscountDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setStartDate(entity.getStartDate());
		dto.setEndDate(entity.getEndDate());
		dto.setActive(entity.isActive());
		
		return dto;
	}
}
