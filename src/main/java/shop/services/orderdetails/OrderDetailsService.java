package shop.services.orderdetails;

import java.util.List;

import shop.dto.order.OrderDetailsDto;

public interface OrderDetailsService {
	
	public List<OrderDetailsDto> findByOrderId(Long orderId);
}
