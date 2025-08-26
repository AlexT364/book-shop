package shop.services.order;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.dto.CheckoutFormDto;
import shop.dto.order.OrderDto;
import shop.exceptions.NotEnoughItemsException;
import shop.exceptions.OrderNotFoundException;
import shop.exceptions.cart.CartIsEmptyException;
import shop.exceptions.user.UserNotFoundException;
import shop.mapping.mappers.OrderMapper;
import shop.persistence.entities.Book;
import shop.persistence.entities.CartItem;
import shop.persistence.entities.Order;
import shop.persistence.entities.OrderDetails;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.OrderDetailsPK;
import shop.persistence.entities.enums.OrderStatus;
import shop.persistence.repositories.CartRepository;
import shop.persistence.repositories.OrderDetailsRepository;
import shop.persistence.repositories.OrderRepository;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.book.BookRepository;
import shop.services.reservation.ReservationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final OrderDetailsRepository orderDetailsRepository;
	private final ReservationService reservationService;
	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public void createOrder(CheckoutFormDto orderRequest, String username) {
		//Load user and its cart
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not found"));
		List<CartItem> cart = this.loadCart(username);
		
		//Load books from users' cart and check availability 
		Map<Long, Integer> quantityByBookId = this.aggregateQuantities(cart);
		Map<Long, Book> booksById = this.loadBooks(quantityByBookId.keySet());
		
		this.checkAndHandleReservations(cart, booksById);
		
		//Build order and orderDetails
		Order orderToCreate = this.initOrder(orderRequest, user);
		List<OrderDetails> orderDetailsList = this.buildOrderDetails(orderToCreate, booksById, quantityByBookId);
		
		this.updateStock(booksById, quantityByBookId);
		
		bookRepository.saveAll(booksById.values());
		orderDetailsRepository.saveAll(orderDetailsList);
		cartRepository.deleteAllByUsername(username);
	}
	
	@Override
	@Transactional
	public void editOrder(OrderDto orderDto) {
		Order orderEntity = orderRepository.findById(orderDto.getId())
				.orElseThrow(() -> new OrderNotFoundException("Order not found.".formatted(orderDto.getId(), orderDto.getUsername())));

		orderEntity.setPhone(orderDto.getPhone());
		OrderStatus newStatus = orderDto.getStatus();
		if(newStatus.equals(OrderStatus.CANCELLED) && !orderEntity.getStatus().equals(OrderStatus.CANCELLED)) {
			this.returnBooksToStock(orderEntity);
		}
		orderEntity.setStatus(orderDto.getStatus());
		orderEntity.setShipAddress(orderDto.getShipAddress());
		orderEntity.setShipCity(orderDto.getShipCity());
		orderEntity.setShipCountry(orderDto.getShipCountry());

		orderRepository.save(orderEntity);
	}

	// --Helper methods--
	
	private OrderDetails createOrderDetails(Order order, Book book, int quantity) {
		OrderDetails od = new OrderDetails();
		OrderDetailsPK pk = new OrderDetailsPK(order.getId(), book.getId());
		od.setPk(pk);
		od.setOrder(order);
		od.setBook(book);
		od.setQuantity(quantity);
		od.setUnitPrice(book.getPrice());
		return od;
	}


	private void returnBooksToStock(Order order) {
		Map<Long, Integer> bookIdsAndQuantity = order.getOrderDetails()
				.stream()
				.collect(Collectors.toMap(orderDetail -> orderDetail.getBook().getId(),
						OrderDetails::getQuantity));
				
		List<Book> booksToUpdateUnitsInStock = bookRepository.findAllByIds(bookIdsAndQuantity.keySet());
		for(Book b : booksToUpdateUnitsInStock) {
			int updatedUnitsInStock = b.getUnitsInStock() + bookIdsAndQuantity.get(b.getId());
			b.setUnitsInStock(updatedUnitsInStock);
		}
	}

	private List<CartItem> loadCart(String username) {
		List<CartItem> userCart = cartRepository.findByUsername(username);
		if (userCart.isEmpty()) {
			throw new CartIsEmptyException("Cart is empty");
		}
		return userCart;
	}
	
	private Order initOrder(CheckoutFormDto orderRequest, User user) {
		Order order = orderMapper.toOrder(orderRequest);
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.NEW);
		return orderRepository.save(order);
	}

	private List<OrderDetails> buildOrderDetails(Order order, Map<Long, Book> booksById, Map<Long, Integer> qtyByBookId) {
		return qtyByBookId.entrySet()
				.stream()
				.map(e -> createOrderDetails(order, booksById.get(e.getKey()), e.getValue()))
				.toList();
	}

	private Map<Long, Book> loadBooks(Collection<Long> ids) {
		return bookRepository.findAllByIds(ids)
				.stream()
				.collect(Collectors.toMap(Book::getId, Function.identity()));
	}
	    
	private Map<Long, Integer> aggregateQuantities(List<CartItem> cart) {
		return cart.stream()
				.collect(Collectors.toMap(
						item -> item.getBook().getId(),
						CartItem::getQuantity)
						);
	}
	
	private void checkAndHandleReservations(List<CartItem> cart, Map<Long, Book> booksById) {
		for(var item : cart) {
			Book book = booksById.get(item.getBook().getId());
			if(book == null) {
				throw new NotEnoughItemsException("Book not found: " + item.getBook().getId());
			}
			
			int available = book.getUnitsInStock() - book.getUnitsReserved();
			if (available < item.getQuantity()) {
				throw new NotEnoughItemsException("Not enough stock for bookId = " + book.getId());
			}
			
			if(item.isExpired()) {
				reservationService.checkAvailability(book, item.getQuantity());
			}
			
			reservationService.releaseReservation(book, item.getQuantity());
		}
	}


	private void updateStock(Map<Long, Book> booksById, Map<Long, Integer> quantityByBookId) {
		quantityByBookId.forEach((bookId, qty) -> {
			Book book = booksById.get(bookId);
			book.setUnitsInStock(book.getUnitsInStock() - qty);
		});
	}

}
