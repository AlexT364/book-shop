package shop.controllers.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;
import shop.controllers.UriAwareController;
import shop.dto.ShopRequestDto;
import shop.dto.author.ShortAuthorDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.security.SecurityUser;
import shop.services.author.AuthorQueryService;
import shop.services.book.BookQueryService;
import shop.services.genre.GenreService;

@Controller
@RequiredArgsConstructor
public class ShopController extends UriAwareController{
	
	private final BookQueryService bookQueryService;
	private final GenreService genresService;
	private final AuthorQueryService authorQueryService;
	
	@ModelAttribute
	public void prepareModel(Model model){
		List<GenreDto> genres = genresService.getAllGenres().stream().map(g -> new GenreDto(g.getId(), g.getName())).toList();
		List<ShortAuthorDto> authors = authorQueryService.getAllAuthorsShort();
		model.addAttribute("genres", genres);
		model.addAttribute("authors", authors);
	}
	
	@GetMapping(path = "/shop")
	public String getShopPage(
			@ModelAttribute(name="filters") ShopRequestDto shopRequestDto,
			Model model,
			@AuthenticationPrincipal SecurityUser securityUser
			) {
		shopRequestDto.normalizePricesAndPaging();
		
		Page<ShortBookDto> books = securityUser == null ?
				bookQueryService.getBooksPageByFilter(shopRequestDto) :
				bookQueryService.getBooksPageByFilter(shopRequestDto, securityUser.getUsername());
		
		if(shopRequestDto.getMaxPrice() == 0) {
			BigDecimal maxPrice = bookQueryService.findMaxPriceByFilters(shopRequestDto).setScale(0, RoundingMode.UP);
			shopRequestDto.setMaxPrice(maxPrice.intValue());
		}
		
		model.addAttribute("books", books);
		model.addAttribute("pageSize", shopRequestDto.getPageSize());
		model.addAttribute("currentPage", books.getNumber());
		return "shop.html";
	}
}
	
