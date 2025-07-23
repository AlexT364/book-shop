package shop.dto.author;

import lombok.Data;

@Data
public class AuthorFilterRequestDto {
	
	private AuthorOrder authorOrder = AuthorOrder.LAST_NAME_ASC;
	private int pageSize;
	private int pageNumber;
	private String lastName;
	
	public void normalizePagination() {
		if(pageNumber < 0) {
			pageNumber = 0;
		}
		if(pageSize < 18) {
			pageSize = 18;
		}
		if(pageSize > 36) {
			pageSize = 48;
		}
	}
	
	public enum AuthorOrder{
		LAST_NAME_ASC,
		LAST_NAME_DESC
	}
}
