package shop.services.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.cart.CartItemDto;
import shop.exceptions.book.BookNotFoundException;
import shop.exceptions.cart.CartItemNotFoundException;
import shop.exceptions.user.UserNotFoundException;
import shop.mapping.mappers.cart.CartItemMapper;
import shop.persistence.entities.Book;
import shop.persistence.entities.CartItem;
import shop.persistence.entities.Discount;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.CartItemPK;
import shop.persistence.repositories.CartRepository;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.book.BookRepository;
import shop.services.discount.DiscountPriceCalculator;
import shop.services.discount.DiscountQueryService;
import shop.services.reservation.ReservationService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ReservationService reservationService;
	private final CartItemMapper cartItemMapper;
	private final DiscountQueryService discountQueryService;
	private final DiscountPriceCalculator discountPriceCalculator;

	@Override
	public List<CartItemDto> getAllBooksInCart(String username) {
		List<CartItem> cartItems = cartRepository.findByUsername(username);
		if (cartItems.isEmpty()) {
			return new ArrayList<CartItemDto>();
		}

		List<Long> bookIds = cartItems.stream().map(cartItem -> cartItem.getBook().getId()).toList();

		Map<Long, Set<Discount>> discountsByBookId = discountQueryService.findDiscountsForBooks(bookIds);

		List<CartItemDto> dtos = cartItems.stream().map(cartItem -> {
			CartItemDto dto = cartItemMapper.toCartItemDto(cartItem);
			dto.setPriceWithDiscount(discountPriceCalculator.calculateBestPrice(discountsByBookId.get(dto.getId()), cartItem.getBook().getPrice()));
			dto.setSubtotalPrice(dto.getPriceWithDiscount().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

			return dto;
		}).toList();

		return dtos;
	}
	
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
		
		// If there are no such item in user's cart
		if (cartItem == null) {
			CartItemPK newCartPk = new CartItemPK(user.getId(), book.getId());
			cartItem = new CartItem(newCartPk, user, book, quantity, LocalDateTime.now(), false);
			reservationService.checkAndReserve(book, quantity);
			cartItem.setExpired(false);
		}
		// If such item is present in users's cart
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

}















