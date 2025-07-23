package shop.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import shop.persistence.entities.Book;
import shop.persistence.entities.CartItem;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.CartItemPK;

public interface CartRepository extends JpaRepository<CartItem, CartItemPK> {
	
	@Modifying
	@Query("""
			DELETE FROM CartItem c WHERE (c.book.id = :id AND c.user.username = :username)
			""")
	public void deleteBookFromCart(long id, String username);
	
	@Query("""
			SELECT c FROM CartItem c  WHERE c.book = :b AND c.user = :u
			""")
	public Optional<CartItem> findByBookAndUser(Book b, User u);
	
	@Query("""
			SELECT c FROM CartItem c
			JOIN c.user u
			JOIN FETCH c.book b
			WHERE u.username = :username
			""")
	public List<CartItem> findByUsername(String username);

	@Modifying
	@Query("""
			DELETE FROM CartItem c WHERE c.user.username = :username
			""")
	public void deleteAllByUsername(String username);

	@Modifying
	@Query("""
			UPDATE CartItem c SET c.expired = true WHERE c.addedAt <= :threshold
			""")
	public void manageExpired(LocalDateTime threshold);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT c FROM CartItem c JOIN FETCH c.book b WHERE (c.expired = false AND c.addedAt <= :threshold)
			""")
	public List<CartItem> findExpired(LocalDateTime threshold);
	
}
