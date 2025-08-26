package shop.controllers.orders;

import java.util.List;

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
import shop.security.SecurityUser;
import shop.services.order.OrderQueryService;
import shop.services.orderdetails.OrderDetailsService;

@Controller
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderQueryService orderQueryService;
	private final OrderDetailsService orderDetailsService;

	@GetMapping
	public String ordersPage(@AuthenticationPrincipal SecurityUser user, Model model, @RequestParam(defaultValue = "0") int pageNumber) {
		Page<OrderDto> orders = orderQueryService
				.getOrderByUsername(user.getUsername(), pageNumber);
		
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orders.getNumber());
		return "orders/orders.html";
	}

	@GetMapping(path = "/{orderId:\\d+}")
	public String userOrder(@PathVariable Long orderId, Model model, @AuthenticationPrincipal SecurityUser securityUser) {
		if(securityUser == null) {
			return "redirect:/login";
		}
		OrderDto orderDto = orderQueryService.getOrderByUsernameAndId(securityUser.getUsername(), orderId);
		List<OrderDetailsDto> orderItems = orderDetailsService.findByOrderId(orderDto.getId());
		
		model.addAttribute("order", orderDto);
		model.addAttribute("orderItems", orderItems);
		return "orders/order-detailed.html";
	}
}






























