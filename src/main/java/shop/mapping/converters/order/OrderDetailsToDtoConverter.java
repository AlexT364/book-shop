package shop.mapping.converters.order;

import java.math.BigDecimal;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.order.OrderDetailsDto;
import shop.persistence.entities.OrderDetails;

public class OrderDetailsToDtoConverter implements Converter<OrderDetails, OrderDetailsDto>{

	@Override
	public OrderDetailsDto convert(MappingContext<OrderDetails, OrderDetailsDto> context) {
		OrderDetailsDto dto = new OrderDetailsDto();
		OrderDetails entity = context.getSource();
		
		dto.setId(entity.getBook().getId());
		dto.setTitle(entity.getBook().getTitle());
		dto.setIsbn(entity.getBook().getIsbn());
		dto.setQuantity(entity.getQuantity());
		dto.setUnitPrice(entity.getUnitPrice());
		BigDecimal subtotalPrice = entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
		dto.setSubtotalPrice(subtotalPrice);
		return dto;
	}

}
