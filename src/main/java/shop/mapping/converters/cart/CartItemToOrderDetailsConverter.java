package shop.mapping.converters.cart;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import shop.persistence.entities.CartItem;
import shop.persistence.entities.OrderDetails;
import shop.persistence.entities.embeddables.OrderDetailsPK;

public class CartItemToOrderDetailsConverter implements Converter<CartItem, OrderDetails> {

	@Override
	public OrderDetails convert(MappingContext<CartItem, OrderDetails> context) {
		OrderDetails orderDetails = new OrderDetails();
		CartItem cartItem = context.getSource();
		orderDetails.setQuantity(cartItem.getQuantity());
		orderDetails.setBook(cartItem.getBook());
		orderDetails.setPk(new OrderDetailsPK(null, cartItem.getBook().getId()));
		orderDetails.setUnitPrice(cartItem.getBook().getPrice());
		return orderDetails;
	}

}
