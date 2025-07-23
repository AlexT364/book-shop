package shop.services.order;

import org.springframework.data.domain.Page;

import shop.dto.CheckoutFormDto;
import shop.dto.order.OrderDto;
import shop.persistence.entities.Order;

public interface OrderService {

	void createOrder(CheckoutFormDto orderToCreate, String username);

	Page<Order> getOrderByUsername(String username, int pageNumber);
	
	Page<Order> getAllOrders(int pageNumber);
	
	Order getOrderByUsernameAndId(String username, Long orderId);

	void editOrder(OrderDto orderDto);
	
	Order getOrderById(Long orderId);

}
