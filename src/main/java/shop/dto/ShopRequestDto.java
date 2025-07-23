package shop.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ShopRequestDto {

	private List<Integer> genres = new ArrayList<>();
	private List<Long> authors = new ArrayList<>();
	private int pageSize;
	private int pageNumber;
	private BookOrdering sort = BookOrdering.DEFAULT;
	private int minPrice;
	private int maxPrice;
	
	public enum BookOrdering {
		
		TITLE_ASC("Title Ascending"), 
		TITLE_DESC("Title Descending"),
		PRICE_ASC("Price Ascending"), 
		PRICE_DESC("Price Descending"),
		DEFAULT("Default");
		
		private final String fieldDescription;
		
		private BookOrdering(String fieldDescription) {
			this.fieldDescription = fieldDescription;
		}
		
		public String toString() {
			return fieldDescription;
		}
	}
	
	public void normalizePricesAndPaging() {
		if(minPrice > maxPrice) {
			minPrice = 0;
			maxPrice = 0;
		}
		if(pageSize < 6) pageSize = 6;
		if(pageSize > 24) pageSize = 24;
		if(pageNumber < 0) pageNumber = 0;
	}
	
}
