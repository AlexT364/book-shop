package shop.services.cart;

import java.util.List;

import shop.persistence.entities.CartItem;

public interface CartService {

	public List<CartItem> getBooksInCart(String username);
	public void removeBook(long bookId, String username);
	public void addBook(long bookId, int quantity, String username);
	public List<CartItem> getAllBooks();
}
