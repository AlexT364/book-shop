package shop.services.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.exceptions.NotEnoughItemsException;
import shop.persistence.entities.Book;
import shop.persistence.entities.CartItem;
import shop.persistence.repositories.CartRepository;
import shop.persistence.repositories.book.BookRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService{

	private final CartRepository cartRepository;
	private static final long FIFTEEN_MINUTES = 15 * 60 * 1000;
	//TODO DELETE LATER. Maybe move to application.properties
	private static final long TEN_SECONDS = 10 * 1000;
	
	/**
	 * Checks amount of books available for reservation and reserves them. 
	 * If amount of available not enough then throws exception.
	 * @param book {@link Book} entity
	 * @param quantity amount of book units to reserve
	 * @throws NotEnoughItemsException if there are not enough books in stock.
	 */
	@Override
	@Transactional
	public void checkAndReserve(Book book, int quantity) {
		int booksAvailable = book.getUnitsInStock() - book.getUnitsReserved();
		if(booksAvailable < quantity) {
			log.error("Reservation failed: bookdId={}, title={}, requested={}, available={}",
					book.getId(),
					book.getTitle(),
					quantity,
					booksAvailable);
			throw new NotEnoughItemsException("Not enough books in stock.");
		}
		book.setUnitsReserved(book.getUnitsReserved() + quantity);
	}

	@Override
	@Transactional
	public void releaseReservation(Book book, int quantity) {
		book.setUnitsReserved(book.getUnitsReserved() - quantity);
	}
	
	@Scheduled(fixedRate = FIFTEEN_MINUTES)
	@Transactional
	public void releaseReservationScheduled() {
		LocalDateTime threshold = LocalDateTime.now().minusSeconds(10);
		List<CartItem> expiredItems = cartRepository.findExpired(threshold);
		
		if(!expiredItems.isEmpty()) {
			for(CartItem cartItem : expiredItems) {
				this.releaseReservation(cartItem.getBook(), cartItem.getQuantity());
				cartItem.setExpired(true);
				cartItem.setAddedAt(null);
			}
			cartRepository.saveAll(expiredItems);
		}
	}
	
	

}
