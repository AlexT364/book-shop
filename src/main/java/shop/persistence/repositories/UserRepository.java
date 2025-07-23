package shop.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("""
			SELECT u FROM User u 
			LEFT JOIN FETCH u.authorities 
			WHERE u.username = :username 
			""")
	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByPassword(String password);
	
	public Optional<User> findByConfirmationToken(String token);
	
	public Optional<User> findByEmail(String email);
	
	public boolean existsByUsername(String username);
	
	public boolean existsByUsernameOrEmail(String username, String email);

	
}
