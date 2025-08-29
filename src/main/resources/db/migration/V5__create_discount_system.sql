CREATE TABLE discounts(
	discount_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	description TEXT,
	amount DECIMAL(10, 2) NOT NULL,
	discount_type VARCHAR(10) NOT NULL CHECK (discount_type IN ('FIXED', 'PERCENT')),
	start_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE discount_books(
	discount_id BIGINT NOT NULL,
	book_id BIGINT NOT NULL,
	
	PRIMARY KEY(discount_id, book_id),
	CONSTRAINT fk_discount_books_discounts FOREIGN KEY(discount_id) REFERENCES discounts(discount_id) ON DELETE CASCADE,
	CONSTRAINT fk_discount_books_books FOREIGN KEY(book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

CREATE TABLE discount_genres(
	discount_id BIGINT NOT NULL,
	genre_id BIGINT NOT NULL,
	
	PRIMARY KEY(discount_id, genre_id),
	CONSTRAINT fk_discount_genres_discounts FOREIGN KEY(discount_id) REFERENCES discounts(discount_id) ON DELETE CASCADE,
	CONSTRAINT fk_discount_genres_genres FOREIGN KEY(genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);




