package shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.persistence.entities.Authority;
import shop.persistence.entities.embeddables.AuthorityPK;

public interface AuthoritiesRepository extends JpaRepository<Authority, AuthorityPK>{
	
}
