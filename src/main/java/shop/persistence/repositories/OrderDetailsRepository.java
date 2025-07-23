package shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.persistence.entities.OrderDetails;
import shop.persistence.entities.embeddables.OrderDetailsPK;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsPK>{
	
	
}
