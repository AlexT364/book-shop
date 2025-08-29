package shop.persistence.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.persistence.entities.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long>{
	
	@Query("""
		    SELECT DISTINCT d
		    FROM Discount d
		    LEFT JOIN d.books b
		    LEFT JOIN d.genres g
		    WHERE d.active = true
		      AND d.startDate <= CURRENT_TIMESTAMP
		      AND d.endDate >= CURRENT_TIMESTAMP
		      AND (
		            b.id = :bookId
		         OR g.id IN (
		             SELECT g2.id
		             FROM Book b2
		             JOIN b2.genres g2
		             WHERE b2.id = :bookId
		         )
		      )
		""")
	public Set<Discount> findDiscountsForBook(Long bookId);
	
	@Query("""
			SELECT DISTINCT b.id, d
			FROM Book b
			JOIN b.discounts d
			WHERE d.active = true
				AND b.id IN :bookIds
				AND d.startDate <= CURRENT_TIMESTAMP
				AND d.endDate >= CURRENT_TIMESTAMP
			""")
	public List<Object[]> findDiscountsForBooks(List<Long> bookIds);
	
	@Query("""
			SELECT DISTINCT b.id, d
			FROM Book b
			JOIN b.genres g
			JOIN g.discounts d
			WHERE d.active = true
				AND b.id IN :bookIds
				AND d.startDate <= CURRENT_TIMESTAMP
				AND d.endDate >= CURRENT_TIMESTAMP
			""")
	public List<Object[]> findDiscountsForBooksGenres(List<Long> bookIds);
	
	@Query("""
			SELECT d
			FROM Discount d
			WHERE d.active = true
				AND d.startDate <= CURRENT_TIMESTAMP
				AND d.endDate >= CURRENT_TIMESTAMP
			""")
	public Page<Discount> findCurrentActiveDiscounts(Pageable pageable);
	
	
	@Query("""
			SELECT d
			FROM Discount d
			ORDER BY
				CASE
					WHEN d.active = true AND d.startDate <= CURRENT_TIMESTAMP AND d.endDate >= CURRENT_TIMESTAMP THEN 1
					WHEN d.startDate > CURRENT_TIMESTAMP THEN 2
					ELSE 3
				END,
				d.startDate DESC
			""")
	public Page<Discount> findAllOrderedForAdmin(Pageable pageable);
	
}
