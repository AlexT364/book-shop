package shop.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="contact_messages")
@Data
public class ContactMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="message_id")
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String name;
	@Column(nullable = false, length = 254)
	private String email;
	@Column(length = 40)
	private String phone;
	@Column(length = 254)
	private String subject;
	@Column(nullable = false)
	private String message;
	@Column(name="added_at", nullable = false)
	private LocalDateTime addedAt;
	private boolean viewed;
	
}
