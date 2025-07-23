package shop.persistence.repositories.contact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long>, CustomMessageRepository{
	
	@Query("""
			SELECT cm FROM ContactMessage cm ORDER BY cm.addedAt DESC
			""")
	public List<ContactMessage> getAll();

}
