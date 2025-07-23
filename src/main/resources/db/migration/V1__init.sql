
--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `confirmation_token` varchar(255) DEFAULT NULL,
  `token_creation_date` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `unique_emails` (`email`),
  UNIQUE KEY `confirmation_token` (`confirmation_token`)
) ;

--
-- Table structure for table `authorities`
--

CREATE TABLE `authorities` (
  `user_id` bigint NOT NULL,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ;

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `bio` text,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
);

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text,
  `isbn` varchar(10) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `units_in_stock` int NOT NULL,
  `units_reserved` int NOT NULL DEFAULT '0',
  `added_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `isbn` (`isbn`)
) ;

--
-- Table structure for table `genres`
--
CREATE TABLE `genres` (
  `genre_id` int NOT NULL,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `name` (`name`)
) ;


--
-- Table structure for table `book_reviews`
--

CREATE TABLE `book_reviews` (
  `user_id` bigint NOT NULL,
  `book_id` int NOT NULL,
  `review` text,
  `added_at` datetime NOT NULL,
  `score` tinyint unsigned DEFAULT NULL,
  PRIMARY KEY (`user_id`,`book_id`),
  KEY `fk_book_reviews_books` (`book_id`),
  CONSTRAINT `fk_book_reviews_books` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_book_reviews_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `chk_score_between_one_and_five` CHECK ((`score` between 1 and 5))
) ;


--
-- Table structure for table `books_authors`
--

CREATE TABLE `books_authors` (
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `fk_books_auhtors_authors` (`author_id`),
  CONSTRAINT `fk_books_auhtors_authors` FOREIGN KEY (`author_id`) REFERENCES `authors` (`author_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_books_auhtors_books` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE RESTRICT
);

--
-- Table structure for table `books_genres`
--

CREATE TABLE `books_genres` (
  `book_id` int NOT NULL,
  `genre_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `books_genres_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `books_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`genre_id`) ON DELETE CASCADE ON UPDATE RESTRICT
);
--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `user_id` bigint NOT NULL,
  `book_id` int NOT NULL,
  `quantity` int NOT NULL,
  `added_at` datetime DEFAULT NULL,
  `expired` tinyint DEFAULT NULL,
  PRIMARY KEY (`user_id`,`book_id`),
  KEY `fk_cart_books` (`book_id`),
  CONSTRAINT `fk_cart_books` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ;

--
-- Table structure for table `contact_messages`
--

CREATE TABLE `contact_messages` (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(254) NOT NULL,
  `phone` varchar(40) DEFAULT NULL,
  `subject` varchar(254) DEFAULT NULL,
  `message` text NOT NULL,
  `added_at` datetime NOT NULL,
  `viewed` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`message_id`)
) ;

--
-- Table structure for table `favourite`
--

CREATE TABLE `favourite` (
  `user_id` bigint NOT NULL,
  `book_id` int NOT NULL,
  `added_at` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`book_id`),
  KEY `fk_favourites_books` (`book_id`),
  CONSTRAINT `fk_favourites_books` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_favourites_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `order_date` datetime NOT NULL,
  `ship_address` varchar(200) DEFAULT NULL,
  `ship_city` varchar(100) NOT NULL,
  `ship_country` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_orders_users` (`user_id`),
  CONSTRAINT `fk_orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `order_id` bigint NOT NULL,
  `book_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `discount` decimal(3,2) DEFAULT '0.00',
  PRIMARY KEY (`order_id`,`book_id`),
  KEY `fk_order_details_books` (`book_id`),
  CONSTRAINT `fk_order_details_books` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_order_details_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


--
-- Table structure for table `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `expires_at` datetime NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token` (`token`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ;

