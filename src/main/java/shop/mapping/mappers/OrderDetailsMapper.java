package shop.mapping.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import shop.dto.order.OrderDetailsDto;
import shop.persistence.entities.OrderDetails;

@Component
public class OrderDetailsMapper {
	
	public OrderDetailsDto toOrderDetailsDto(OrderDetails entity) {
		OrderDetailsDto dto = new OrderDetailsDto();
		
		dto.setBookId(entity.getBook().getId());
		dto.setTitle(entity.getBook().getTitle());
		dto.setIsbn(entity.getBook().getIsbn());
		dto.setQuantity(entity.getQuantity());
		dto.setUnitPrice(entity.getUnitPrice());
		
		BigDecimal subtotalPrice = entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
		dto.setSubtotalPrice(subtotalPrice);
		
		return dto;
	}
}
