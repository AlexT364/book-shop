package shop.services.orderdetails;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.persistence.entities.OrderDetails;
import shop.persistence.repositories.OrderDetailsRepository;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService{
	
	private final OrderDetailsRepository orderDetailsRepository;
	
	@Override
	public List<OrderDetails> findByOrderId(Long orderId) {
		
		return null;
	}

}
