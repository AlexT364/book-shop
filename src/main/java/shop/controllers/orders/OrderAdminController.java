package shop.controllers.orders;

import java.util.List;

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
import shop.services.order.OrderQueryService;
import shop.services.order.OrderService;
import shop.services.orderdetails.OrderDetailsService;

@Controller
@RequestMapping(path = "/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {
	
	private final OrderService orderService;
	private final OrderQueryService orderQueryService;
	private final OrderDetailsService orderDetailsService;
	
	//TODO: Return dtos from service, not entities
	@GetMapping
	public String adminOrdersPage(Model model, @RequestParam(defaultValue = "0") int pageNumber) {
		Page<OrderDto> orders = orderQueryService
				.getAllOrders(pageNumber);
		
		
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orders.getNumber());
		
		return "orders/orders.html";
	}
	
	
	@GetMapping(path = "/{orderId:\\d+}")
	public String userOrder(@PathVariable Long orderId, Model model) {
		OrderDto orderDto = orderQueryService.getOrderById(orderId);
		List<OrderDetailsDto> orderItems = orderDetailsService.findByOrderId(orderDto.getId());
		
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





























