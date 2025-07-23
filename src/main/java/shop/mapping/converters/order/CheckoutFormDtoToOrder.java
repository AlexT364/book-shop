package shop.mapping.converters.order;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.CheckoutFormDto;
import shop.persistence.entities.Order;

public class CheckoutFormDtoToOrder implements Converter<CheckoutFormDto, Order>{
	
	@Override
	public Order convert(MappingContext<CheckoutFormDto, Order> context) {
		CheckoutFormDto dto = context.getSource();
		Order entity = new Order();
		entity.setShipAddress(dto.getShipAddress());
		entity.setShipCity(dto.getShipCity());
		entity.setShipCountry(dto.getShipCountry());
		entity.setPhone(dto.getPhone());
		
		return entity;
	}
}
