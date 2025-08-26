package shop.services.order;

import org.springframework.data.domain.Page;

import shop.dto.order.OrderDto;

public interface OrderQueryService {

	Page<OrderDto> getOrderByUsername(String username, int pageNumber);
	
	Page<OrderDto> getAllOrders(int pageNumber);
	
	OrderDto getOrderById(Long orderId);
	
	OrderDto getOrderByUsernameAndId(String username, Long orderId);
}
