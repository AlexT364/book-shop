package shop.services.orderdetails;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.dto.order.OrderDetailsDto;
import shop.mapping.mappers.order.OrderDetailsMapper;
import shop.persistence.entities.OrderDetails;
import shop.persistence.repositories.OrderDetailsRepository;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService{
	
	private final OrderDetailsRepository orderDetailsRepository;
	private final OrderDetailsMapper orderDetailsMapper;
	
	@Override
	public List<OrderDetailsDto> findByOrderId(Long orderId) {
		List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(orderId);
		List<OrderDetailsDto> dtos = orderDetails.stream().map(orderDetailsMapper::toOrderDetailsDto).toList();
		return dtos;
	}

}
