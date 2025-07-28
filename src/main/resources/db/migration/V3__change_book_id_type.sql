ALTER TABLE book_reviews
DROP FOREIGN KEY fk_book_reviews_books;

ALTER TABLE books_authors
DROP FOREIGN KEY fk_books_auhtors_books;

ALTER TABLE books_genres
DROP FOREIGN KEY books_genres_ibfk_1;

ALTER TABLE cart
DROP FOREIGN KEY fk_cart_books;

ALTER TABLE favourite
DROP FOREIGN KEY fk_favourites_books;

ALTER TABLE order_details
DROP FOREIGN KEY fk_order_details_books;



ALTER TABLE books_authors
MODIFY COLUMN book_id BIGINT;

ALTER TABLE books_genres
MODIFY COLUMN book_id BIGINT;

ALTER TABLE book_reviews
MODIFY COLUMN book_id BIGINT;

ALTER TABLE cart
MODIFY COLUMN book_id BIGINT;

ALTER TABLE favourite
MODIFY COLUMN book_id BIGINT;

ALTER TABLE order_details
MODIFY COLUMN book_id BIGINT;

ALTER TABLE books
MODIFY COLUMN book_id BIGINT;


ALTER TABLE book_reviews
ADD CONSTRAINT fk_book_reviews_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;

ALTER TABLE books_authors
ADD CONSTRAINT fk_books_authors_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;

ALTER TABLE books_genres
ADD CONSTRAINT fk_books_genres_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;

ALTER TABLE cart
ADD CONSTRAINT fk_cart_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;

ALTER TABLE favourite
ADD CONSTRAINT fk_favourites_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;

ALTER TABLE order_details
ADD CONSTRAINT fk_order_details_books
FOREIGN KEY(book_id)
REFERENCES books(book_id)
ON DELETE CASCADE;
