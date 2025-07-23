FROM openjdk:21-slim

WORKDIR /app

COPY book_covers book_covers
COPY author_images author_images


COPY target/book-store-1.0.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar"]