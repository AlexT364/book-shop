package shop.persistence.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.dto.author.ShortAuthorDto;
import shop.persistence.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	@Query("""
			SELECT new shop.dto.author.ShortAuthorDto(
				a.id, 
				CONCAT(a.firstName, ' ', a.lastName)
			) 
			FROM Author a
			WHERE (:lastName IS NULL OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
			""")
	public Page<ShortAuthorDto> findAllAsShortDto(String lastName, Pageable pageable);
	
	@Query("""
			SELECT new shop.dto.author.ShortAuthorDto(
				a.id,
				CONCAT(a.firstName, ' ', a.lastName)
			)
			FROM Author a
			ORDER BY a.lastName ASC
			""")
	public List<ShortAuthorDto> findAllSortByName();
	
	@Query("SELECT a FROM Author a WHERE a.id IN :authorIds")
	public Set<Author> findAuthorsByIds(Set<Long> authorIds);
	
	public boolean existsByLastName(String name);
}
