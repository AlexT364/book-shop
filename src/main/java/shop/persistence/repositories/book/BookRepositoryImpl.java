package shop.persistence.repositories.book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import shop.dto.ShopRequestDto;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Genre;

public class BookRepositoryImpl implements CustomBookRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Book> findCriteriaBooks(Pageable pageable, ShopRequestDto request, boolean onlyAvailable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Book> mainQuery = criteriaBuilder.createQuery(Book.class);
		Root<Book> mainRoot = mainQuery.from(Book.class);
		
		List<Predicate> predicates = this.buildPredicates(criteriaBuilder, mainRoot, request, onlyAvailable);
		
		mainQuery.where(predicates.toArray(new Predicate[0]));
		
		List<Order> orders = new ArrayList<Order>();
		
		// First show books that available. Books that are out of stock appear last.
		if(!onlyAvailable) {
			Expression<Integer> stockOrder = criteriaBuilder.<Integer>selectCase()
					.when(criteriaBuilder.equal(mainRoot.get("unitsInStock"), 0), 1)
					.otherwise(0);
			orders.add(criteriaBuilder.asc(stockOrder));
		}
		
		for(org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
			if(order.isAscending()) {
				orders.add(criteriaBuilder.asc(mainRoot.get(order.getProperty())));
			}else {
				orders.add(criteriaBuilder.desc(mainRoot.get(order.getProperty())));
			}
		}
		
		mainQuery.orderBy(orders);
		
		TypedQuery<Book> typedQuery = entityManager.createQuery(mainQuery);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());
		
		List<Book> result = typedQuery.getResultList();
		
		//Count Query
		CriteriaQuery<Long> countQueryBuilder = criteriaBuilder.createQuery(Long.class);
		Root<Book> countRoot = countQueryBuilder.from(Book.class);
		
		List<Predicate> countPredicates = this.buildPredicates(criteriaBuilder, countRoot, request, onlyAvailable);
		
		countQueryBuilder.select(criteriaBuilder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
		Long total = entityManager.createQuery(countQueryBuilder).getSingleResult();
		
		return new PageImpl<Book>(result, pageable, total);
	}
	
	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Book> root, ShopRequestDto request, boolean onlyAvailable){
		int minPrice = request.getMinPrice();
		int maxPrice = request.getMaxPrice();
		List<Integer> genreIds = request.getGenres();
		List<Long> authorsIds = request.getAuthors();
		List<Predicate> predicates = new ArrayList<>();
		
		if(onlyAvailable) {
			predicates.add(cb.greaterThan(root.get("unitsInStock"), 0));
		}
		
		if(maxPrice > 0) {
			predicates.add(cb.between(root.get("price"), minPrice, maxPrice));
		}
		if(genreIds != null && !genreIds.isEmpty()) {
			Join<Book, Genre> genreJoin = root.join("genres");
			predicates.add(genreJoin.get("id").in(genreIds));
		}
		if(authorsIds != null && !authorsIds.isEmpty()) {
			Join<Book, Author> authorJoin = root.join("authors");
			predicates.add(authorJoin.get("id").in(authorsIds));
		}
		return predicates;
	}
	
	@Override
	public BigDecimal findMaxPriceWithFilters(ShopRequestDto request) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);
		Root<Book> root = query.from(Book.class);
		
		List<Predicate> predicates = this.buildPredicates(builder, root, request, false);
		
		query.select(builder.max(root.get("price")))
		.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(query);
		
		
		try {
			BigDecimal result = typedQuery.getSingleResult();
			return result == null ? BigDecimal.ZERO : result;
		}catch(NoResultException e) {
			return BigDecimal.ZERO;
		}
	}
	
	
	
	@Override
	@Transactional
	public Optional<Book> findByIdWithAuthorsAndGenres(Long id) {
		
		 EntityGraph<Book> graph = entityManager.createEntityGraph(Book.class);
		    graph.addAttributeNodes("authors", "genres");
		    
		    Map<String, Object> hints = new HashMap<>();
		    hints.put("jakarta.persistence.fetchgraph", graph);
		    
		    Book book = entityManager.find(Book.class, id, hints);
		    return Optional.ofNullable(book);
	}
	
	
}
