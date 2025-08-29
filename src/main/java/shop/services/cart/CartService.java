package shop.services.cart;

import java.util.List;

import shop.dto.cart.CartItemDto;

public interface CartService {

	public List<CartItemDto> getAllBooksInCart(String username);
	public void removeBook(long bookId, String username);
	public void addBook(long bookId, int quantity, String username);
}
