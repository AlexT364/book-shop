package shop.mapping.mappers.order;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.CheckoutFormDto;
import shop.dto.order.OrderDto;
import shop.persistence.entities.Order;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	
	public OrderDto toOrderDto(Order entity) {
		OrderDto dto = new OrderDto();
		
		dto.setId(entity.getId());
		dto.setUsername(entity.getUser().getUsername());
		dto.setOrderDate(entity.getOrderDate().toLocalDate());
		dto.setNumOfItems(entity.getOrderDetails()
				.stream()
				.map(orderDetails -> orderDetails.getQuantity())
				.reduce(0, Integer::sum));
		
		dto.setShipAddress(entity.getShipAddress());
		dto.setShipCity(entity.getShipCity());
		dto.setShipCountry(entity.getShipCountry());
		dto.setPhone(entity.getPhone());
		dto.setTotalPrice(entity.getOrderDetails()
				.stream()
				.map(orderDetails -> orderDetails.getUnitPrice().multiply(BigDecimal.valueOf(orderDetails.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		
		dto.setStatus(entity.getStatus());
		return dto;
	}
	
	public Order toOrder(CheckoutFormDto dto) {
		Order entity = new Order();
		
		entity.setShipAddress(dto.getShipAddress());
		entity.setShipCountry(dto.getShipCountry());
		entity.setShipCity(dto.getShipCity());
		entity.setPhone(dto.getPhone());
		
		return entity;
	}
}
