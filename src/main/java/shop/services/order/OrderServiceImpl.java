package shop.services.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.dto.CheckoutFormDto;
import shop.dto.order.OrderDto;
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
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not found"));
		//Loading items from user's cart
		List<CartItem> cart = cartRepository.findByUsername(username);
		if (cart.isEmpty()) {
			throw new CartIsEmptyException("Cart is empty");
		}
		
		//Creating order
		Order order = orderMapper.toOrder(orderRequest);
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.NEW);
		order = orderRepository.save(order);
		
		List<Long> bookIds = cart.stream()
				.map(cartItem -> cartItem.getId().getBookId())
				.toList();
		List<Book> booksToUpdateUnitsInStock = bookRepository.findAllByIds(bookIds);
		Map<Long, Book> booksInCart = booksToUpdateUnitsInStock.stream()
				.collect(Collectors.toMap(Book::getId, Function.identity()));

		List<OrderDetails> orderDetailsList = new ArrayList<>();

		//Checking book availability and managing reserves
		for (CartItem cartItem : cart) {
			Long bookId = cartItem.getBook().getId();
			Book book = booksInCart.get(bookId);
			if (cartItem.isExpired()) {
				reservationService.checkAndReserve(book, cartItem.getQuantity());
			}

			int newUnitsInStock = book.getUnitsInStock() - cartItem.getQuantity();
			book.setUnitsInStock(newUnitsInStock);
			reservationService.releaseReservation(book, cartItem.getQuantity());
			
			OrderDetails od = this.createOrderDetails(order, book, cartItem.getQuantity());
			orderDetailsList.add(od);
			
		}
		bookRepository.saveAll(booksToUpdateUnitsInStock);
		orderDetailsRepository.saveAll(orderDetailsList);
		cartRepository.deleteAllByUsername(username);

	}
	
	@Override
	public Page<Order> getOrderByUsername(String username, int pageNumber) {
		Pageable byOrderDateDesc = PageRequest.of(pageNumber, 10, Sort.by("orderDate").descending());
		return orderRepository.findUsersOrders(byOrderDateDesc, username);
	}

	@Override
	public Order getOrderByUsernameAndId(String username, Long orderId) {
		Order order = orderRepository.findByIdAndUsername(orderId, username).orElseThrow(
				() -> new OrderNotFoundException("Order with id: %d not found or access denied.".formatted(orderId)));
		return order;
	}

	@Override
	@Transactional
	public Page<Order> getAllOrders(int pageNumber) {
		Pageable byOrderDateDesc = PageRequest.of(pageNumber, 10, Sort.by("orderDate").descending());
		Page<Order> orders = orderRepository.findAll(byOrderDateDesc);
		return orders;
	}

	@Override
	@Transactional
	public void editOrder(OrderDto orderDto) {
		Order orderEntity = orderRepository.findByIdAndUsername(orderDto.getId(), orderDto.getUsername())
				.orElseThrow(() -> new OrderNotFoundException("Order not found."
						.formatted(orderDto.getId(), orderDto.getUsername())));

		orderEntity.setPhone(orderDto.getPhone());
		OrderStatus newStatus = orderDto.getStatus();
		if(newStatus.equals(OrderStatus.CANCELLED) && !orderEntity.getStatus().equals(OrderStatus.CANCELLED)) {
			this.returnBooksToStock(orderEntity);
		}
		orderEntity.setStatus(orderDto.getStatus());
		//
		orderEntity.setShipAddress(orderDto.getShipAddress());
		orderEntity.setShipCity(orderDto.getShipCity());
		orderEntity.setShipCountry(orderDto.getShipCountry());

		orderRepository.save(orderEntity);
	}

	@Override
	public Order getOrderById(Long orderId) {
		Order orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: %d not found.".formatted(orderId)));

		return orderEntity;
	}
	
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
}
