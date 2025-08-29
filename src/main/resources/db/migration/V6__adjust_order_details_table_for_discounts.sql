UPDATE order_details
SET discount = 0.00
WHERE discount IS NULL;

ALTER TABLE order_details
CHANGE discount discount_unit DECIMAL(10,2) DEFAULT 0.00 NOT NULL;

ALTER TABLE order_details
ADD COLUMN final_unit DECIMAL(10,2) DEFAULT 0.00 NOT NULL;

ALTER TABLE order_details
ADD COLUMN subtotal DECIMAL(10,2) DEFAULT 0.00 NOT NULL;

UPDATE order_details
SET final_unit = unit_price - discount_unit,
	subtotal = (unit_price - discount_unit) * quantity;