package shop.services.discount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.dto.discount.DiscountDto;
import shop.mapping.mappers.discount.DiscountMapper;
import shop.persistence.entities.Discount;
import shop.persistence.repositories.DiscountRepository;

@Service
@RequiredArgsConstructor
public class DiscountQueryServiceImpl implements DiscountQueryService{

	private final DiscountRepository discountRepository;
	private final DiscountMapper discountMapper;
	
	@Override
	public Map<Long, Set<Discount>> findDiscountsForBooks(List<Long> bookIds) {
		List<Object[]> allDiscountRows = Stream.concat(
				discountRepository.findDiscountsForBooks(bookIds).stream(), 
				discountRepository.findDiscountsForBooksGenres(bookIds).stream())
				.toList();
		
		Map<Long, Set<Discount>> discountsByBookId = allDiscountRows.stream()
				.collect(Collectors.groupingBy(
						row -> (Long) row[0], //Key - Long bookId
						Collectors.mapping(row -> (Discount)row[1], Collectors.toSet()) //Value - Set<Discount>
						));
		bookIds.forEach(id -> discountsByBookId.putIfAbsent(id, Set.of()));
		
		return discountsByBookId;
	}

	@Override
	public Set<Discount> findDiscountsForBook(Long bookId) {
		Set<Discount> discounts = discountRepository.findDiscountsForBook(bookId);
		return discounts;
	}

	@Override
	public Page<DiscountDto> findCurrentActiveDiscounts(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		Page<Discount> page = discountRepository.findCurrentActiveDiscounts(pageable);
		return page.map(discountMapper::toDiscountDto);
	}

	@Override
	public Page<DiscountDto> findDiscountsForAdmin(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		Page<Discount> page = discountRepository.findAll(pageable);
		
		Page<DiscountDto> dtoPage = page.map(discount -> {
			DiscountDto dto = discountMapper.toDiscountDto(discount);
			dto.setExpired(dto.getEndDate().isBefore(LocalDateTime.now()));
			return dto;
		});
		return dtoPage;
	}
	
	
}
