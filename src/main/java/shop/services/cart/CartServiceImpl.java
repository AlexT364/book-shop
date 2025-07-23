package shop.services.cart;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.exceptions.book.BookNotFoundException;
import shop.exceptions.cart.CartItemNotFoundException;
import shop.exceptions.user.UserNotFoundException;
import shop.persistence.entities.Book;
import shop.persistence.entities.CartItem;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.CartItemPK;
import shop.persistence.repositories.CartRepository;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.book.BookRepository;
import shop.services.reservation.ReservationService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ReservationService reservationService;

	@Override
	@Transactional
	public void removeBook(long bookId, String username) {
		Book entity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book Not Found"));
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UserNotFoundException("User with username: \"%s\" not found.".formatted(username)));
		CartItem bookToRemove = cartRepository.findByBookAndUser(entity, user).orElseThrow(() -> new CartItemNotFoundException("Error on deleting item from cart."));
		if(!bookToRemove.isExpired()) {
			reservationService.releaseReservation(entity, bookToRemove.getQuantity());
		}
		cartRepository.deleteBookFromCart(bookId, username);
	}
	
	
	@Transactional
	@Override
	public void addBook(long bookId, int quantity, String username) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found."));
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found."));
		CartItem cartItem = cartRepository.findByBookAndUser(book, user).orElse(null);
		
		// Если товара нет в корзине пользователя
		if (cartItem == null) {
			CartItemPK newCartPk = new CartItemPK(user.getId(), book.getId());
			cartItem = new CartItem(newCartPk, user, book, quantity, LocalDateTime.now(), false);
			reservationService.checkAndReserve(book, quantity);
			cartItem.setExpired(false);
		}
		// Если товар есть в корзине.
		else {
			int reserveQuantity = cartItem.isExpired() ? (cartItem.getQuantity() + quantity) : quantity;
			reservationService.checkAndReserve(book, reserveQuantity);
			
			int newQuantity = cartItem.getQuantity() + quantity;
			cartItem.setAddedAt(LocalDateTime.now());
			cartItem.setQuantity(newQuantity);
			cartItem.setExpired(false);
		}
		cartRepository.save(cartItem);
	}

	
	@Override
	public List<CartItem> getBooksInCart(String username) {
		return cartRepository.findByUsername(username);
	}
	
	public List<CartItem> getAllBooks(){
		return cartRepository.findAll();
	}

}















