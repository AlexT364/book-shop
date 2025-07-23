package shop.persistence.repositories.contact;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import shop.dto.contact.InboxMessagesFilterDto;
import shop.dto.contact.ShortContactMessageDto;
import shop.persistence.entities.ContactMessage;

public class ContactMessageRepositoryImpl implements CustomMessageRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ShortContactMessageDto> findCriteriaMessages(Pageable pageable, InboxMessagesFilterDto inboxFilterRequest) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShortContactMessageDto> mainQuery = criteriaBuilder.createQuery(ShortContactMessageDto.class);
		Root<ContactMessage> mainRoot = mainQuery.from(ContactMessage.class);

		//Filtration
		List<Predicate> predicates = this.buildPredicates(criteriaBuilder, mainRoot, inboxFilterRequest);
		mainQuery.where(predicates.toArray(new Predicate[0]));

		//Ordering
		List<Order> orders = new ArrayList<>();
		for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
			if (order.isAscending()) {
				orders.add(criteriaBuilder.asc(mainRoot.get(order.getProperty())));
			} else {
				orders.add(criteriaBuilder.desc(mainRoot.get(order.getProperty())));
			}
		}
		mainQuery.orderBy(orders);
		mainQuery.select(criteriaBuilder.construct(ShortContactMessageDto.class, 
				mainRoot.get("id"),
				mainRoot.get("name"),
				mainRoot.get("email"),
				mainRoot.get("subject"),
				mainRoot.get("addedAt"),
				mainRoot.get("viewed")
				));
		TypedQuery<ShortContactMessageDto> typedQuery = entityManager.createQuery(mainQuery);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());
		List<ShortContactMessageDto> result = typedQuery.getResultList();
		

		// CountQuery
		CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ContactMessage> countRoot = countCriteriaQuery.from(ContactMessage.class);

		List<Predicate> countPredicates = this.buildPredicates(criteriaBuilder, countRoot, inboxFilterRequest);

		countCriteriaQuery.select(criteriaBuilder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
		Long total = entityManager.createQuery(countCriteriaQuery).getSingleResult();

		return new PageImpl<ShortContactMessageDto>(result, pageable, total);
	}

	private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<ContactMessage> root, InboxMessagesFilterDto inboxFilterRequest){
		List<Predicate> predicates = new ArrayList<>();
		if (inboxFilterRequest.getEmail() != null && !inboxFilterRequest.getEmail().isEmpty()
				&& !inboxFilterRequest.getEmail().isBlank()) {
			predicates.add(criteriaBuilder.like(root.get("email"), "%" + inboxFilterRequest.getEmail() + "%"));
		}

		switch (inboxFilterRequest.getViewedSort()) {
		case ALL:
			break;
		case UNVIEWED:
			predicates.add(criteriaBuilder.equal(root.get("viewed"), false));
			break;
		case VIEWED:
			predicates.add(criteriaBuilder.equal(root.get("viewed"), true));
			break;
		}
		return predicates;
	}
}
