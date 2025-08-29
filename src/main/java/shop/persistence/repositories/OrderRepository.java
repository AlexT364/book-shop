package shop.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Query("""
			   SELECT o.id FROM Order o
			   WHERE o.user.username = :username
			""")
	public Page<Long> findOrderIds(Pageable pageable, String username);
		
	@Query("""
			SELECT o.id FROM Order o
			""")
	public Page<Long> findAllIds(Pageable pageable);
	
	@EntityGraph(attributePaths = {"orderDetails"})
	@Query("""
			   SELECT o from Order o
			   WHERE o.id in :ids
			   ORDER BY o.orderDate DESC
			""")
	public List<Order> findOrdersWithDetailsByIds(List<Long> ids);
	
	@EntityGraph(attributePaths = {"orderDetails", "user"})
	@Query("""
			SELECT o FROM Order o 
			WHERE o.user.username = :username AND o.id = :orderId
			""")
	public Optional<Order> findByIdAndUsername(Long orderId, String username);

	@Query("""
			SELECT o FROM Order o JOIN FETCH o.user
			""")
	public Page<Order> getAll(Pageable byOrderDateDesc);

	@Query("""
			SELECT o FROM Order o
			JOIN FETCH o.user u
			JOIN FETCH o.orderDetails od
			JOIN FETCH od.book b
			WHERE o.id = :orderId
			""")
	public Optional<Order> findById(Long orderId);

	
}
