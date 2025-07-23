package shop.mapping.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.CheckoutFormDto;
import shop.persistence.entities.Order;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	
	private final ModelMapper modelMapper;
	
	public Order toOrder(CheckoutFormDto dto) {
		return modelMapper.map(dto, Order.class);
	}
}
