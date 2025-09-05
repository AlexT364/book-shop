
INSERT INTO `users` (user_id, username, password, enabled, email, confirmation_token, token_creation_date)
VALUES 
(1,'admin','$2a$12$nboRV0HdHcN1upAg541GLeeK7JKPOaOA72gO9zgJUjDgYYex94oau', 1,'admin@mail.com',NULL,NULL),
(2,'bill','$2a$12$JhXxeGMdlc/wvntv2Oupd.lQ3Sa.X1gwFB0C9RN4CqzJyL9OMM6KS', 1,'bill@mail.com',NULL,NULL),
(3,'kate', '$2a$12$84xSpLjo3jwjfsshe56ERe7MWf6NXzjP2u5oXU6omK9ZWdCJyDQPO', 1, 'kate@testmail.com', NULL, NULL);

INSERT INTO `authorities` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

INSERT INTO `books`(book_id, title, description, isbn, price, units_in_stock, units_reserved, added_at) VALUES 
(1,'War and Peace','War and peace description', '1400079985', 14.95, 10, 0, '2025-06-19 18:53:18'),
(2,'Cherry Orchard','Cherry Orchard description', '1350501727', 7.41, 5, 0, '2025-06-19 18:53:18'),
(3,'Master and Margarita','Master and Margarita description', '0143108271', 29.44, 8, 0, '2025-06-19 18:53:18'),
(4,'Anna Karenina','Anna Karenina description', '0198800533', 30.12, 0, 0, '2025-06-19 18:53:18'),
(5,'Government Inspector','Government Inspector description', '184022729X', 16.22, 1, 0, '2025-06-19 18:53:18'),
(6,'Crime and Punishment','Crime and Punishment description', '0679734503', 21.68, 0, 0, '2025-06-19 18:53:18'),
(7,'Thinking in Java. 3rd Edition','Thinking in Java. 3rd Edition description', '0131002872', 6.95, 7, 0, '2025-06-19 18:53:18'),
(8,'Dune','Dune description', '044100590X', 11.00, 13, 0, '2025-06-19 18:53:18'),
(9,'Learning Web Design: A Beginner\''s Guide to HTML, CSS, JavaScript, and Web Graphics', 'Learning Web Design description', '1491960205', 49.58, 11, 0, '2025-06-19 18:53:18'),
(10,'The Idiot','The Idiot description', '1853261750', 37.13, 42, 0, '2025-06-19 18:53:18'),
(11,'Lord of the Rings: The Fellowship of the Ring','description', '0008567123', 21.28, 100, 0, '2025-06-19 18:53:18'),
(12,'Lord of the Rings: The Two Towers','description', '0063412624', 18.00, 99, 0, '2025-06-19 18:53:18'),
(13,'Lord of the Rings: The Return of the King','description', '0063412632', 17.50, 96, 0, '2025-06-19 18:53:18'),
(14,'Atomic Kotlin','description', '0981872557', 44.91, 60, 0, '2025-06-19 18:53:18'),
(15,'Heart of a Dog','description', '0802150594', 7.24, 48, 0, '2025-06-19 18:53:18'),
(16,'The Idiot','description', '0241739829', 32.00, 75, 0, '2025-06-19 18:53:18'),
(17,'The Adventures of Tom Sawyer: Original Illustrations','description', '1955529191', 17.95, 50, 0, '2025-06-19 20:08:46'),
(18,'Adventures of Huckleberry Finn','description', '1956221697', 19.95, 50, 0, '2025-06-19 20:19:02'),
(19,'A Study in Scarlet (1891 Illustrated Edition)','description', '195552999X', 15.95, 80, 0, '2025-06-19 23:39:03'),
(20,'The Sign of Four (1892 Illustrated Edition)','description', '195622100X', 15.95, 74, 0, '2025-06-19 23:44:10'),
(21,'The Adventures of Sherlock Holmes (100th Anniversary Edition)','description', '1956221018', 18.95, 140, 0, '2025-06-19 23:47:01'),
(22,'A Game of Thrones','description', '0553103547', 16.44, 27, 0, '2025-06-24 01:19:46'),
(23,'A Clash of Kings','description', '0553108034', 15.45, 35, 0, '2025-06-25 00:28:08'),
(24,'A Storm of Swords','description', '0553106635', 13.00, 40, 0, '2025-06-28 13:12:33'),
(25,'A Feast for Crows','description', '0553801503', 14.45, 50, 0, '2025-06-30 21:01:39');

INSERT INTO `authors`(author_id, bio, first_name, last_name) VALUES 
(1,'bio', 'Leo', 'Tolstoy'),
(2,'bio', 'Anton', 'Chekhov'),
(3,'bio', 'Mikhail', 'Bulgakov'),
(4,'bio', 'Nikolai', 'Gogol'),
(5,'bio', 'Fyodor', 'Dostoyevsky'),
(6,null,  'Bruce', 'Eckel'),
(7,null,  'Frank', 'Herbert'),
(8,'bio', 'Jennifer', 'Robbins'),
(9,'bio', 'J.R.R', 'Tolkien'),
(10,'bio', 'J.K.', 'Rowling'),
(11,null,  'George R.R.', 'Martin'),
(12,'bio', 'Mark', 'Twain'),
(13,null,  'Arthur', 'Conan Doyle');

INSERT INTO `genres`(genre_id, name) VALUES 
(1,'Fiction'),
(2,'History'),
(4,'Learning'),
(5,'Programming'),
(3,'Sci-fi');

INSERT INTO `book_reviews`(user_id, book_id, review, added_at, score) VALUES 
(2, 8,'very good book, really enjoyed it','2025-04-13 16:11:01', 5),
(2, 4, null, '2025-04-13 21:21:21', 5),
(2, 1, null, '2025-04-13 21:21:21', 4),
(2, 5, null, '2025-04-13 21:21:21', 3),
(3, 15, null, '2025-04-13 21:21:21', 1),
(3, 5, null, '2025-04-13 21:21:21', 2),
(3, 10, null, '2025-04-13 21:21:21', 4),
(3, 2, null, '2025-04-13 21:21:21', 5);

INSERT INTO `books_authors`(book_id, author_id) VALUES 
(1,1),
(2,2),
(3,3),
(4,4),
(5,4),
(6,5),
(7,6),
(8,7),
(9,8),
(10,5),
(11,9),
(12,9),
(13,9),
(14,6),
(15,3),
(16,5),
(17,12),
(18,12),
(19,13),
(20,13),
(21,13),
(22,11),
(23,11),
(24,11);

INSERT INTO `books_genres`(book_id, genre_id) VALUES 
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,4),
(7,5),
(8,1),
(8,3),
(9,4),
(9,5),
(10,1),
(11,1),
(12,1),
(13,1),
(14,4),
(14,5),
(15,1),
(16,1),
(17,1),
(18,1),
(19,1),
(20,1),
(21,1),
(22,1),
(23,1),
(24,1),
(25,1);




