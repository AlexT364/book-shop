package shop.mapping.converters.order;

import java.math.BigDecimal;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.dto.order.OrderDto;
import shop.persistence.entities.Order;

public class OrderToDtoConverter implements Converter<Order, OrderDto>{

	@Override
	public OrderDto convert(MappingContext<Order, OrderDto> context) {
		OrderDto orderDto = new OrderDto();
		Order order = context.getSource();
		orderDto.setId(order.getId());
		orderDto.setUsername(order.getUser().getUsername());
		orderDto.setOrderDate(order.getOrderDate().toLocalDate());
		orderDto.setNumOfItems(order.getOrderDetails().stream().map(od -> od.getQuantity()).reduce(Integer::sum).get());
		orderDto.setShipAddress(order.getShipAddress());
		orderDto.setShipCity(order.getShipCity());
		orderDto.setShipCountry(order.getShipCountry());
		orderDto.setPhone(order.getPhone());
		orderDto.setTotalPrice(
				order
				.getOrderDetails()
				.stream()
				.map(orderDetails -> orderDetails.getUnitPrice().multiply(BigDecimal.valueOf(orderDetails.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		orderDto.setStatus(order.getStatus());
		return orderDto;
	}

}
