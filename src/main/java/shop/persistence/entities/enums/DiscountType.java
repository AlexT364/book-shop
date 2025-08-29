package shop.persistence.entities.enums;

public enum DiscountType {
	FIXED("Fixed amount"), 
	PERCENT("Percentage");
	
	private final String fieldDescription;
	
	private DiscountType(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}
	
	public String toString() {
		return fieldDescription;
	}
}
