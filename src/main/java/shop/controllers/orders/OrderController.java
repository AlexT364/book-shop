package shop.controllers.orders;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.dto.order.OrderDetailsDto;
import shop.dto.order.OrderDto;
import shop.persistence.entities.Order;
import shop.security.SecurityUser;
import shop.services.order.OrderService;

@Controller
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final ModelMapper modelMapper;

	@GetMapping
	public String ordersPage(@AuthenticationPrincipal SecurityUser user, Model model, @RequestParam(defaultValue = "0") int pageNumber) {
		Page<OrderDto> orders = orderService
				.getOrderByUsername(user.getUsername(), pageNumber)
				.map(order -> modelMapper.map(order, OrderDto.class));
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orders.getNumber());
		return "orders/orders.html";
	}

	//TODO OrderService.getOrderByUsernameAndId возращает entity, лучше проработать вариант возврата DTO
	@GetMapping(path = "/{orderId:\\d+}")
	public String userOrder(@PathVariable Long orderId, Model model, @AuthenticationPrincipal SecurityUser user) {
		Order orderEntity = new Order();
		orderEntity = orderService.getOrderByUsernameAndId(user.getUsername(), orderId);

		List<OrderDetailsDto> orderItems = orderEntity.getOrderDetails().stream()
				.map(orderDetail -> modelMapper.map(orderDetail, OrderDetailsDto.class)).toList();
		OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
		
		model.addAttribute("order", orderDto);
		model.addAttribute("orderItems", orderItems);
		return "orders/order-detailed.html";
	}
}






























