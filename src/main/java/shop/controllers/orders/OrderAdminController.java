package shop.controllers.orders;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.dto.order.OrderDetailsDto;
import shop.dto.order.OrderDto;
import shop.persistence.entities.Order;
import shop.services.order.OrderService;

@Controller
@RequestMapping(path = "/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {
	
	private final OrderService orderService;
	private final ModelMapper modelMapper;
	
	// TODO ADD PAGINATION
	@GetMapping
	public String adminOrdersPage(Model model, @RequestParam(defaultValue = "0") int pageNumber) {
		Page<Order> ordersEnt = orderService
				.getAllOrders(pageNumber);
		
		Page<OrderDto> orders = ordersEnt.map(order -> modelMapper.map(order, OrderDto.class));
				
		
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orders.getNumber());
		
		return "orders/orders.html";
	}
	
	
	@GetMapping(path = "/{orderId:\\d+}")
	public String userOrder(@PathVariable Long orderId, Model model) {
		Order orderEntity = null;
		orderEntity = orderService.getOrderById(orderId);

		List<OrderDetailsDto> orderItems = orderEntity.getOrderDetails().stream()
				.map(orderDetail -> modelMapper.map(orderDetail, OrderDetailsDto.class)).toList();
		OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
		
		model.addAttribute("order", orderDto);
		model.addAttribute("orderItems", orderItems);
		return "orders/order-detailed.html";
	}
	
	@PutMapping(path = "/{orderId:\\d+}")
	public String editOrder(@ModelAttribute(name = "order") OrderDto orderDto, BindingResult bindingResult) {
		orderService.editOrder(orderDto);
		
		return "redirect:/admin/orders/%d".formatted(orderDto.getId());
	}
	
}





























