package shop.services.order;

import shop.dto.CheckoutFormDto;
import shop.dto.order.OrderDto;

public interface OrderService {

	void createOrder(CheckoutFormDto orderToCreate, String username);

	void editOrder(OrderDto orderDto);
	

}
