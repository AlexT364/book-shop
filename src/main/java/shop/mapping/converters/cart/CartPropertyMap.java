package shop.mapping.converters.cart;

import java.math.BigDecimal;

import org.modelmapper.PropertyMap;

import shop.dto.cart.CartItemDto;
import shop.persistence.entities.CartItem;

public class CartPropertyMap extends PropertyMap<CartItem, CartItemDto> {

	@Override
	protected void configure() {
		map().setId(source.getBook().getId());
		map().setTitle(source.getBook().getTitle());
		map().setIsbn(source.getBook().getIsbn());
		map().setPrice(source.getBook().getPrice());
		map().setQuantity(source.getQuantity());
		using(cnv -> {
			CartItem source = (CartItem) cnv.getSource();
			return source.getBook().getPrice().multiply(BigDecimal.valueOf(source.getQuantity()));
		}).map(source).setSubtotalPrice(null);
	}

}
