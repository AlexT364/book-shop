package shop.controllers.sales;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.dto.discount.CreateDiscountDto;
import shop.services.discount.DiscountService;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/sales")
public class SalesAdminActionController {
	
	private final DiscountService discountService;

	@PostMapping
	public String createSale(@Valid CreateDiscountDto newDiscountRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "sales/admin/sales-add.html";
		}
		discountService.create(newDiscountRequest);
		return "redirect:/sales";
	}

	@DeleteMapping(path = "/{discountId}")
	public String deleteSale(@PathVariable Long discountId) {
		discountService.delete(discountId);

		return "redirect:/sales";
	}
	
	@PatchMapping(path="/{discountId}/activate")
	public String activateSale(@PathVariable Long discountId) {
		discountService.activateDiscount(discountId);
		
		return "redirect:/admin/sales";
	}
	
	@PatchMapping(path="/{discountId}/deactivate")
	public String deactivateSale(@PathVariable Long discountId) {
		discountService.deactivateDiscount(discountId);
		
		return "redirect:/admin/sales";
	}
}
