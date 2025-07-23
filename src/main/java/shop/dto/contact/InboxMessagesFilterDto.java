package shop.dto.contact;

import lombok.Data;

@Data
public class InboxMessagesFilterDto {
	
	private ViewedSort viewedSort = ViewedSort.ALL;
	private DateOrder dateOrder = DateOrder.NEW_FIRST;
	private int pageNumber = 0;
	private int pageSize = 12;
	private String email;
	
	public void normalizePagination() {
		if(pageNumber < 0) {
			pageNumber = 0;
		}
		if(pageSize < 12) {
			pageSize = 12;
		}
		if(pageSize > 48) {
			pageSize = 48;
		}
	}
	public static enum ViewedSort {
		ALL,
		VIEWED,
		UNVIEWED
	}
	
	public static enum DateOrder{
		NEW_FIRST,
		OLD_FIRST
	}
}
