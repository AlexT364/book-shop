package shop.persistence.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Query("""
			SELECT o FROM Order o 
			JOIN FETCH o.user u 
			WHERE u.username = :username
			ORDER BY o.orderDate desc
			""")
	public Page<Order> findUsersOrders(Pageable pageable, String username);

	@Query("""
			SELECT o FROM Order o JOIN FETCH o.user u WHERE o.user.username = :username AND o.id = :orderId
			""")
	public Optional<Order> findByIdAndUsername(Long orderId, String username);

	@Query("""
			SELECT o FROM Order o JOIN FETCH o.user
			""")
	public Page<Order> getAll(Pageable byOrderDateDesc);

	@Query("""
			SELECT o FROM Order o 
			JOIN FETCH o.user u
			""")
	public Page<Order> findAll(Pageable pageable);
	
	@Query("""
			SELECT o FROM Order o
			JOIN FETCH o.user u
			JOIN FETCH o.orderDetails od
			JOIN FETCH od.book b
			WHERE o.id = :orderId
			""")
	public Optional<Order> findById(Long orderId);
	
}
