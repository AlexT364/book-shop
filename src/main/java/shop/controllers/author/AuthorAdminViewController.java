package shop.controllers.author;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import shop.dto.author.CreateEditAuthorDto;
import shop.services.author.AuthorQueryService;

@Controller
@RequestMapping(path = "/admin/authors")
@RequiredArgsConstructor
public class AuthorAdminViewController {
	
	private final AuthorQueryService authorQueryService;
	
	@ModelAttribute
	public void addAuthorDtoToModel(@PathVariable(required = false) Long authorId, Model model) {
		CreateEditAuthorDto author = authorId == null ? new CreateEditAuthorDto() : authorQueryService.getAuthorByIdForEdit(authorId);
		model.addAttribute("author", author);
	}
	
	@GetMapping("/add")
	public String authorAddPage() {
		return "authors/author-add.html";
	}
	
	@GetMapping("/{authorId:\\d+}")
	public String authorEditPage(@PathVariable Long authorId, Model model) {
		return "authors/author-edit.html";
	}
}
