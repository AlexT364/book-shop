package shop.persistence.repositories.favourite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import shop.dto.ShopRequestDto;
import shop.persistence.entities.Author;
import shop.persistence.entities.Book;
import shop.persistence.entities.Favourite;
import shop.persistence.entities.Genre;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.FavouritePK;

public class FavouriteRepositoryImpl implements CustomFavouriteRepository{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public Page<Favourite> findFavouriteBooks(ShopRequestDto request, String username, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		
		//PK's query
		CriteriaQuery<FavouritePK> primaryKeysQuery = cb.createQuery(FavouritePK.class);
		Root<Favourite> rootPK = primaryKeysQuery.from(Favourite.class);
		Join<Favourite, User> userJoin = rootPK.join("user");
		Join<Favourite, Book> bookJoin = rootPK.join("book");
		
		List<Predicate> predicates = this.buildPredicates(cb, bookJoin, userJoin, request, username);
		
		primaryKeysQuery.select(rootPK.get("id")).where(predicates.toArray(new Predicate[0]));
		TypedQuery<FavouritePK> typedQuery = entityManager.createQuery(primaryKeysQuery);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());
		
		List<FavouritePK> favouritePrimaryKeys = typedQuery.getResultList();
		if (favouritePrimaryKeys.isEmpty()) {
			return new PageImpl<Favourite>(Collections.emptyList(), pageable, 0L);
		}
		
		//Main query
		CriteriaQuery<Favourite> mainQuery = cb.createQuery(Favourite.class);
		Root<Favourite> mainRoot = mainQuery.from(Favourite.class);
		mainRoot.fetch("book").fetch("genres");
		
		List<Order> orders = new ArrayList<Order>();
		
		if (pageable.getSort() != null)
			for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
				if (order.isAscending()) {
					orders.add(cb.asc(mainRoot.get("book").get(order.getProperty())));
				} else {
					orders.add(cb.desc(mainRoot.get("book").get(order.getProperty())));
				}
			}
		
		mainQuery.orderBy(orders);
		
		mainQuery.select(mainRoot).where(mainRoot.get("id").in(favouritePrimaryKeys));
		List<Favourite> favouriteBooks = entityManager.createQuery(mainQuery).getResultList();
		
		//Count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Favourite> countRoot = countQuery.from(Favourite.class);
		Join<Favourite, User> countUserJoin = countRoot.join("user");
		Join<Favourite, Book> countBookJoin = countRoot.join("book");
		
		List<Predicate> countPredicates = this.buildPredicates(cb, countBookJoin, countUserJoin, request, username);
		
		countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
		
		Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
		
		return new PageImpl<>(favouriteBooks, pageable, totalCount);
	}
	
	private List<Predicate> buildPredicates(CriteriaBuilder cb, Join<Favourite, Book> bookJoin, Join<Favourite, User> userJoin, ShopRequestDto request, String username){
		int minPrice = request.getMinPrice();
		int maxPrice = request.getMaxPrice();
		List<Integer> genreIds = request.getGenres();
		List<Long> authorIds = request.getAuthors();
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(cb.equal(userJoin.get("username"), username));
		
		if(maxPrice > 0) {
			predicates.add(cb.between(bookJoin.get("price"), minPrice, maxPrice));
		}
		if(genreIds != null && !genreIds.isEmpty()) {
			Join<Book, Genre> genreJoin = bookJoin.join("genres");
			predicates.add(genreJoin.get("id").in(genreIds));
		}
		if(authorIds != null && !authorIds.isEmpty()) {
			Join<Book, Author> authorJoin = bookJoin.join("authors");
			predicates.add(authorJoin.get("id").in(authorIds));
		}
		return predicates;
	}


	@Override
	public BigDecimal findMaxPriceOfFavourites(ShopRequestDto request, String username, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);
		Root<Favourite> root = query.from(Favourite.class);
		Join<Favourite, Book> bookJoin = root.join("book");
		Join<Favourite, User> userJoin = root.join("user");
		
		List<Predicate> predicates = this.buildPredicates(builder, bookJoin, userJoin, request, username);
		

		query.select(builder.max(root.get("book").get("price"))).where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(query);
		
		try {
			BigDecimal result = typedQuery.getSingleResult();
			return result == null ? BigDecimal.ZERO : result;
		}catch(NoResultException e) {
			return BigDecimal.ZERO;
		}
	}
}


























