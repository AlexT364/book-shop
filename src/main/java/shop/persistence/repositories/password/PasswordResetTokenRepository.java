package shop.persistence.repositories.password;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	@Query("""
			SELECT pt FROM PasswordResetToken pt WHERE pt.token = :token
			""")
	Optional<PasswordResetToken> findByToken(String token);
	
	@Query("""
			SELECT pt FROM PasswordResetToken pt JOIN FETCH pt.user WHERE pt.token = :token
			""")
	Optional<PasswordResetToken> findByTokenWithUser(String token);
}
