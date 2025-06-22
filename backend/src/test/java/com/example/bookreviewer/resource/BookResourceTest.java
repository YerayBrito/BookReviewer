package com.example.bookreviewer.resource;

import com.example.bookreviewer.entity.Book;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BookResourceTest {

    @Test
    public void testCreateAndListBooks() {
        // 1. Create a new Book
        Book newBook = new Book();
        newBook.title = "The Lord of the Rings";
        newBook.author = "J.R.R. Tolkien";

        given()
            .contentType(ContentType.JSON)
            .body(newBook)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .body("title", equalTo("The Lord of the Rings"))
            .body("author", equalTo("J.R.R. Tolkien"));

        // 2. Verify the new book is in the list of all books
        given()
        .when()
            .get("/api/books")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("title", hasItem("The Lord of the Rings"));
    }

    @Test
    public void testUpdateBook() {
        Book newBook = new Book();
        newBook.title = "Original Title";
        newBook.author = "Original Author";

        Long bookId = given()
            .contentType(ContentType.JSON)
            .body(newBook)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        Book updatedBook = new Book();
        updatedBook.title = "Updated Title";
        updatedBook.author = "Updated Author";

        given()
            .contentType(ContentType.JSON)
            .body(updatedBook)
        .when()
            .put("/api/books/" + bookId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Title"))
            .body("author", equalTo("Updated Author"))
            .body("id", equalTo(bookId.intValue()));
    }

    @Test
    public void testUpdateBookNotFound() {
        Book updatedBook = new Book();
        updatedBook.title = "Updated Title";
        updatedBook.author = "Updated Author";

        given()
            .contentType(ContentType.JSON)
            .body(updatedBook)
        .when()
            .put("/api/books/9999")
        .then()
            .statusCode(404)
            .body(containsString("Book not found"));
    }

    @Test
    public void testDeleteBook() {
        Book newBook = new Book();
        newBook.title = "Book to Delete";
        newBook.author = "Author to Delete";

        Long bookId = given()
            .contentType(ContentType.JSON)
            .body(newBook)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .delete("/api/books/" + bookId)
        .then()
            .statusCode(204);

        given()
        .when()
            .get("/api/books/" + bookId)
        .then()
            .statusCode(404)
            .body(containsString("Book not found"));
    }

    @Test
    public void testDeleteBookNotFound() {
        given()
        .when()
            .delete("/api/books/9999")
        .then()
            .statusCode(404)
            .body(containsString("Book not found"));
    }

    @Test
    public void testGetBookById() {
        Book newBook = new Book();
        newBook.title = "Test Book";
        newBook.author = "Test Author";

        Long bookId = given()
            .contentType(ContentType.JSON)
            .body(newBook)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .get("/api/books/" + bookId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Test Book"))
            .body("author", equalTo("Test Author"))
            .body("id", equalTo(bookId.intValue()));
    }

    @Test
    public void testGetBookByIdNotFound() {
        given()
        .when()
            .get("/api/books/9999")
        .then()
            .statusCode(404)
            .body(containsString("Book not found"));
    }
} 