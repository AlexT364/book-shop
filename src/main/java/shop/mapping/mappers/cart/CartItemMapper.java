package shop.mapping.mappers.cart;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.dto.cart.CartItemDto;
import shop.persistence.entities.CartItem;

@Component
@RequiredArgsConstructor
public class CartItemMapper {
	
	public CartItemDto toCartItemDto(CartItem entity) {
		CartItemDto dto = new CartItemDto();
		dto.setId(entity.getBook().getId());
		dto.setIsbn(entity.getBook().getIsbn());
		dto.setPrice(entity.getBook().getPrice());
		dto.setQuantity(entity.getQuantity());
		dto.setTitle(entity.getBook().getTitle());
		
		return dto;
	}
}
