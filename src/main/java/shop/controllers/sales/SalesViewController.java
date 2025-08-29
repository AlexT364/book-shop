package shop.controllers.sales;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.discount.DiscountDto;
import shop.services.discount.DiscountQueryService;

@Controller
@RequestMapping(path="/sales")
@RequiredArgsConstructor
public class SalesViewController extends UriAwareController{
	
	private final DiscountQueryService discountQueryService;
	
	@GetMapping
	public String salesPage(Model model, @RequestParam(name = "pageNum", defaultValue = "0") int pageNumber) {
		if(pageNumber < 0) pageNumber = 0;
		Page<DiscountDto> discounts = discountQueryService.findCurrentActiveDiscounts(pageNumber);
		model.addAttribute("discounts", discounts);
		return "sales/sales.html";
	}
}
