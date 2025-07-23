package shop.persistence.repositories.favourite;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import shop.dto.author.ShortAuthorDto;
import shop.dto.genre.GenreDto;
import shop.persistence.entities.Favourite;
import shop.persistence.entities.embeddables.FavouritePK;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouritePK>, CustomFavouriteRepository{
	
	@Modifying
	@Query("""
			DELETE FROM Favourite f WHERE f.user.username = :username AND f.book.id = :bookId
			""")
	void deleteByIdAndUsername(Long bookId, String username);

	@Query("""
			SELECT f.book.id FROM Favourite f WHERE f.user.username = :username AND f.book.id IN :bookIds
			""")
	Set<Long> findFavouriteBookIds(String username, List<Long> bookIds);

	@Query("""
			SELECT COUNT(f) > 0 FROM Favourite f WHERE f.user.username = :username AND f.book.id = :bookId
			""")
	boolean existsByUsernameAndId(String username, Long bookId);

	@Query("""
			SELECT DISTINCT new shop.dto.genre.GenreDto(g.id, g.name) FROM Favourite f
			JOIN f.book b
			JOIN b.genres g
			WHERE f.user.username = :username
			""")
	List<GenreDto> findDistinctGenresInUserFavourites(String username);
	
	@Query("""
			SELECT DISTINCT new shop.dto.author.ShortAuthorDto
			(
			a.id, 
			CONCAT(a.firstName, ' ', a.lastName) 
			)
			FROM Favourite f
			JOIN f.book b
			JOIN b.authors a
			WHERE f.user.username = :username
			""")
	List<ShortAuthorDto> findDistinctAuthorsInUserFavourites(String username);
}
