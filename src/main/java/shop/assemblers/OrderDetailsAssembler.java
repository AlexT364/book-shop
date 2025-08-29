package shop.assemblers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.persistence.entities.Book;
import shop.persistence.entities.Discount;
import shop.persistence.entities.Order;
import shop.persistence.entities.OrderDetails;
import shop.persistence.entities.embeddables.OrderDetailsPK;
import shop.services.discount.DiscountPriceCalculator;
import shop.services.discount.DiscountQueryService;

@Component
@RequiredArgsConstructor
public class OrderDetailsAssembler {
	
	private final DiscountQueryService discountQueryService;
	private final DiscountPriceCalculator discountPriceCalculator;
	
	public List<OrderDetails> assembleOrderDetails(
			Order order, 
			Map<Long, Book> booksById, 
			Map<Long, Integer> qtyByBookId) {
		
		Map<Long, Set<Discount>> bookIdDiscounts = discountQueryService.findDiscountsForBooks(new ArrayList<Long>(booksById.keySet())); 
		
		Map<Long, BigDecimal> finalPrices = bookIdDiscounts.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						e -> discountPriceCalculator.calculateBestPrice(
								e.getValue(), 
								booksById.get(e.getKey()).getPrice()
								)
						));
		
		List<OrderDetails> orderDetailsList = new ArrayList<>();
		for(Map.Entry<Long, Book> entry : booksById.entrySet()) {
			Long bookId = entry.getKey();
			Book book = entry.getValue();
			Integer quantity = qtyByBookId.get(bookId);
			BigDecimal finalBookPrice = finalPrices.get(bookId);

			OrderDetails od = assembleOrderDetails(order, book, quantity, finalBookPrice);
			
			orderDetailsList.add(od);
		}
		
		return orderDetailsList;
	}

	private OrderDetails assembleOrderDetails(Order order, Book book, Integer quantity, BigDecimal finalBookPrice) {
		OrderDetails od = new OrderDetails();
		OrderDetailsPK pk = new OrderDetailsPK(order.getId(), book.getId());
		
		od.setPk(pk);
		od.setOrder(order);
		od.setBook(book);
		od.setQuantity(quantity);
		od.setUnitPrice(book.getPrice());
		od.setDiscountPerUnit(book.getPrice().max(finalBookPrice).subtract(finalBookPrice));
		od.setFinalUnitPrice(finalBookPrice);
		od.setSubtotal(od.getFinalUnitPrice().multiply(BigDecimal.valueOf(quantity)));
		
		return od;
	}
	
}
