package shop.services.favourite;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.dto.ShopRequestDto;
import shop.dto.author.ShortAuthorDto;
import shop.dto.book.ShortBookDto;
import shop.dto.genre.GenreDto;
import shop.exceptions.book.BookNotFoundException;
import shop.exceptions.user.UserNotFoundException;
import shop.persistence.entities.Book;
import shop.persistence.entities.Favourite;
import shop.persistence.entities.User;
import shop.persistence.entities.embeddables.FavouritePK;
import shop.persistence.repositories.UserRepository;
import shop.persistence.repositories.book.BookRepository;
import shop.persistence.repositories.favourite.FavouriteRepository;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

	private final FavouriteRepository favouriteRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final ModelMapper modelMapper;

	@Override
	public Page<ShortBookDto> getFavouriteBooks(ShopRequestDto request, String username) {
		Pageable pageable = formFavouritePageable(request);
		Page<Favourite> favouritesEntities = favouriteRepository.findFavouriteBooks(request, username, pageable);
		Page<ShortBookDto> favouritesDtos = favouritesEntities.map(favourite -> modelMapper.map(favourite, ShortBookDto.class));
		return favouritesDtos;
	}
	
	@Override
	public BigDecimal getMaxPriceOfFavourites(ShopRequestDto request, String username) {
		Pageable pageable = formFavouritePageable(request);
		BigDecimal result = favouriteRepository.findMaxPriceOfFavourites(request, username, pageable);
		return result;
	}

	@Override
	@Transactional
	public void addBook(String username, Long bookId) {
		User userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User with username: %s not found.".formatted(username)));
		Book bookEntity = bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with id: %d not found.".formatted(bookId)));

		FavouritePK favPk = new FavouritePK(userEntity.getId(), bookEntity.getId());

		Favourite favourite = favouriteRepository.findById(favPk)
				.orElse(new Favourite(favPk, userEntity, bookEntity, LocalDateTime.now()));

		favouriteRepository.save(favourite);
	}

	@Override
	@Transactional
	public void deleteBook(Long bookId, String username) {
		favouriteRepository.deleteByIdAndUsername(bookId, username);
	}

	@Override
	public Set<Long> findFavouriteBookIdsForUser(String username, List<Long> bookIds) {
		Set<Long> favouriteIds = favouriteRepository.findFavouriteBookIds(username, bookIds);
		return favouriteIds;
	}

	@Override
	public boolean checkIfBookInUsersFavourites(String username, Long bookId) {
		return favouriteRepository.existsByUsernameAndId(username, bookId);
	}
	
	@Override
	public List<GenreDto> getDistinctGenresInUserFavourites(String username) {
		return favouriteRepository.findDistinctGenresInUserFavourites(username);
	}
	
	@Override
	public List<ShortAuthorDto> getDistinctAuthorsInUserFavourites(String username) {
		return favouriteRepository.findDistinctAuthorsInUserFavourites(username);
	}
	
	private Pageable formFavouritePageable(ShopRequestDto request) {
		Order order = switch (request.getSort()) {
		case PRICE_ASC -> Order.asc("price");
		case PRICE_DESC -> Order.desc("price");
		case TITLE_ASC -> Order.asc("title");
		case TITLE_DESC -> Order.desc("title");
		default -> new Order(null, "id");
		};

		Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(order));
		return pageable;
	}



}
