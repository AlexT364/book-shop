package shop.persistence.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.enums.OrderStatus;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime orderDate;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private List<OrderDetails> orderDetails;
	
	private String shipAddress;
	private String shipCity;
	private String shipCountry;
	private String phone;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	@Override
	public String toString() {
		return "OrderDetails is null? = " + (orderDetails == null) + "Order [id=" + id + ", user=" + user + ", orderDate=" + orderDate + ", shipAddress=" + shipAddress + ", shipCity=" + shipCity + ", shipCountry=" + shipCountry
				+ ", phone=" + phone + ", status=" + status + "]";
	}
	
}









