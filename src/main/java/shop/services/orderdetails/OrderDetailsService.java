package shop.services.orderdetails;

import java.util.List;

import shop.persistence.entities.OrderDetails;

public interface OrderDetailsService {
	
	public List<OrderDetails> findByOrderId(Long orderId);
}
