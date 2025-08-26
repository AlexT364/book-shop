package shop.services.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.order.OrderDto;
import shop.exceptions.OrderNotFoundException;
import shop.mapping.mappers.OrderMapper;
import shop.persistence.entities.Order;
import shop.persistence.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService{
	
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	
	@Override
	@Transactional(readOnly = true)
	public Page<OrderDto> getOrderByUsername(String username, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("orderDate").descending());
		
		Page<Long> ids = orderRepository.findOrderIds(pageable, username);
		
		List<Order> orders = orderRepository.findOrdersWithDetailsByIds(ids.getContent());
		List<OrderDto> dtos = orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();

		
		return new PageImpl<>(dtos, pageable, ids.getTotalElements());
	}

	@Override
	public OrderDto getOrderByUsernameAndId(String username, Long orderId) {
		Order order = orderRepository.findByIdAndUsername(orderId, username)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: %d not found or access denied.".formatted(orderId)));
		OrderDto dto = orderMapper.toOrderDto(order);
		
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderDto> getAllOrders(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("orderDate").descending());
		
		Page<Long> ids = orderRepository.findAllIds(pageable);
		List<Order> orders = orderRepository.findOrdersWithDetailsByIds(ids.getContent());
		
		List<OrderDto> dtos = orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
		
		return new PageImpl<OrderDto>(dtos, pageable, ids.getTotalElements());
	}
	
	@Override
	public OrderDto getOrderById(Long orderId) {
		Order order= orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: %d not found.".formatted(orderId)));

		OrderDto dto = orderMapper.toOrderDto(order);
		
		return dto;
	}

}
