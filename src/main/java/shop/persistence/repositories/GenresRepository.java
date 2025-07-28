package shop.persistence.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.dto.genre.GenreDto;
import shop.persistence.entities.Genre;

public interface GenresRepository extends JpaRepository<Genre, Long>{
	
	@Query("""
			SELECT new shop.dto.genre.GenreDto(g.id, g.name) FROM Genre g ORDER BY g.name ASC
			""")
	public List<GenreDto> findAllSortByName();
	
	@Query("SELECT g FROM Genre g WHERE g.id IN :genreIds")
    Set<Genre> findGenresByIds(Set<Long> genreIds);
}
