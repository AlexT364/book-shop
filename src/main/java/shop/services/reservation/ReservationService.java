package shop.services.reservation;

import shop.persistence.entities.Book;

public interface ReservationService {

	void checkAndReserve(Book book, int quantity);
	void releaseReservation(Book book, int quantity);
}
