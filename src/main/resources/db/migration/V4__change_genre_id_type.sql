ALTER TABLE books_genres
DROP FOREIGN KEY books_genres_ibfk_2;



ALTER TABLE books_genres
MODIFY COLUMN genre_id BIGINT;

ALTER TABLE genres
MODIFY COLUMN genre_id BIGINT;


ALTER TABLE books_genres
ADD CONSTRAINT fk_books_genres_genres
FOREIGN KEY(genre_id)
REFERENCES genres(genre_id)
ON DELETE CASCADE;
 