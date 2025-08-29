package shop.controllers.book;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.persistence.entities.Book;
import shop.persistence.repositories.book.BookRepository;

@RestController
@RequestMapping("/admin/api/books")
@RequiredArgsConstructor
public class BookAdminRestController {
	//TODO: REPLACE LATER WITH BookService
	private final BookRepository bookRepository;

    @GetMapping("/search")
    public List<BookForSearchDto> searchBooks(@RequestParam String query) {
        List<Book> dtos = bookRepository.findTop10ByTitleContainingIgnoreCase(query);
        
        return dtos.stream().map(b -> new BookForSearchDto(b.getId(), b.getTitle())).toList();
    }
    
    public record BookForSearchDto(Long id, String title) {}

}
