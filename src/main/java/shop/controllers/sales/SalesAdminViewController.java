package shop.controllers.sales;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import shop.dto.discount.CreateDiscountDto;
import shop.dto.discount.DiscountDto;
import shop.persistence.entities.enums.DiscountType;
import shop.services.discount.DiscountQueryService;
import shop.services.genre.GenreService;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/sales")
public class SalesAdminViewController {
	
	private final GenreService genreService;
	private final DiscountQueryService discountQueryService;
	
	@GetMapping
	public String adminSalesPage(
			Model model, 
			@RequestParam(defaultValue = "0") int pageNumber
			){
		if (pageNumber < 0) pageNumber = 0;
		Page<DiscountDto> discounts = discountQueryService.findDiscountsForAdmin(pageNumber);
		
		model.addAttribute("discounts", discounts);
		
		return "sales/admin/sales-admin.html";
	}

	@GetMapping(path = "/add")
	public String addSalePage(Model model) {
		CreateDiscountDto discountRequest = new CreateDiscountDto();

		model.addAttribute("discountRequest", discountRequest);
		model.addAttribute("discountTypes", DiscountType.values());
		model.addAttribute("genres", genreService.getAllGenres());
		return "sales/admin/sales-add.html";
	}
}
