package shop.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.Order;
import shop.persistence.entities.OrderDetails;
import shop.persistence.entities.embeddables.OrderDetailsPK;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsPK>{
	
	@EntityGraph(attributePaths = {"book", "order"})
	@Query("""
			SELECT od
			FROM OrderDetails od
			WHERE od.order.id = :orderId
			""")
	List<OrderDetails> findAllByOrderId(Long orderId);

	@EntityGraph(attributePaths = {"order"})
	@Query("""
			SELECT od 
			FROM OrderDetails od
			WHERE od.order in :orders
			""")
	List<OrderDetails> findDetailsForOrders(List<Order> orders);
}
