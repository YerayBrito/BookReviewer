package com.example.bookreviewer.resource;

import com.example.bookreviewer.dto.ReviewDTO;
import com.example.bookreviewer.entity.Book;
import com.example.bookreviewer.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ReviewResourceTest {

    private User testUser;
    private Book testBook;

    @BeforeEach
    @Transactional
    public void setup() {
        testUser = new User();
        testUser.username = "testuser";
        testUser.email = "testuser@example.com";
        testUser.persist();

        testBook = new Book();
        testBook.title = "Test Book";
        testBook.author = "Test Author";
        testBook.persist();
    }

    @Test
    public void testAddReview() {
        ReviewDTO dto = new ReviewDTO();
        dto.rating = 5;
        dto.comment = "Excellent!";
        dto.userId = testUser.id;
        dto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201)
            .body("rating", equalTo(5))
            .body("comment", equalTo("Excellent!"))
            .body("user.id", equalTo(testUser.id.intValue()))
            .body("book.id", equalTo(testBook.id.intValue()));
    }

    @Test
    public void testAddReview_UserOrBookNotFound() {
        ReviewDTO dto = new ReviewDTO();
        dto.rating = 4;
        dto.comment = "Not found test";
        dto.userId = 9999L;
        dto.bookId = 9999L;

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testUpdateReview() {
        ReviewDTO createDto = new ReviewDTO();
        createDto.rating = 3;
        createDto.comment = "Original comment";
        createDto.userId = testUser.id;
        createDto.bookId = testBook.id;

        Long reviewId = given()
            .contentType(ContentType.JSON)
            .body(createDto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        ReviewDTO updateDto = new ReviewDTO();
        updateDto.rating = 5;
        updateDto.comment = "Updated comment";
        updateDto.userId = testUser.id;
        updateDto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(updateDto)
        .when()
            .put("/api/reviews/" + reviewId)
        .then()
            .statusCode(200)
            .body("rating", equalTo(5))
            .body("comment", equalTo("Updated comment"))
            .body("id", equalTo(reviewId.intValue()));
    }

    @Test
    public void testUpdateReviewNotFound() {
        ReviewDTO updateDto = new ReviewDTO();
        updateDto.rating = 5;
        updateDto.comment = "Updated comment";
        updateDto.userId = testUser.id;
        updateDto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(updateDto)
        .when()
            .put("/api/reviews/9999")
        .then()
            .statusCode(404)
            .body(containsString("Review not found"));
    }

    @Test
    public void testDeleteReview() {
        ReviewDTO createDto = new ReviewDTO();
        createDto.rating = 4;
        createDto.comment = "Review to delete";
        createDto.userId = testUser.id;
        createDto.bookId = testBook.id;

        Long reviewId = given()
            .contentType(ContentType.JSON)
            .body(createDto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .delete("/api/reviews/" + reviewId)
        .then()
            .statusCode(204);

        given()
        .when()
            .get("/api/reviews/" + reviewId)
        .then()
            .statusCode(404)
            .body(containsString("Review not found"));
    }

    @Test
    public void testDeleteReviewNotFound() {
        given()
        .when()
            .delete("/api/reviews/9999")
        .then()
            .statusCode(404)
            .body(containsString("Review not found"));
    }

    @Test
    public void testGetReviewById() {
        ReviewDTO createDto = new ReviewDTO();
        createDto.rating = 4;
        createDto.comment = "Test review";
        createDto.userId = testUser.id;
        createDto.bookId = testBook.id;

        Long reviewId = given()
            .contentType(ContentType.JSON)
            .body(createDto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .get("/api/reviews/" + reviewId)
        .then()
            .statusCode(200)
            .body("rating", equalTo(4))
            .body("comment", equalTo("Test review"))
            .body("id", equalTo(reviewId.intValue()));
    }

    @Test
    public void testGetReviewByIdNotFound() {
        given()
        .when()
            .get("/api/reviews/9999")
        .then()
            .statusCode(404)
            .body(containsString("Review not found"));
    }

    @Test
    public void testGetAllReviews() {
        given()
        .when()
            .get("/api/reviews")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetReviewsByBook() {
        ReviewDTO createDto = new ReviewDTO();
        createDto.rating = 4;
        createDto.comment = "Book review";
        createDto.userId = testUser.id;
        createDto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(createDto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201);

        given()
        .when()
            .get("/api/reviews/book/" + testBook.id)
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("book.id", hasItem(testBook.id.intValue()));
    }

    @Test
    public void testGetReviewsByBookNotFound() {
        given()
        .when()
            .get("/api/reviews/book/9999")
        .then()
            .statusCode(404)
            .body(containsString("Book not found"));
    }

    @Test
    public void testGetReviewsByUser() {
        ReviewDTO createDto = new ReviewDTO();
        createDto.rating = 4;
        createDto.comment = "User review";
        createDto.userId = testUser.id;
        createDto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(createDto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201);

        given()
        .when()
            .get("/api/reviews/user/" + testUser.id)
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("user.id", hasItem(testUser.id.intValue()));
    }

    @Test
    public void testGetReviewsByUserNotFound() {
        given()
        .when()
            .get("/api/reviews/user/9999")
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testAddDuplicateReview() {
        ReviewDTO dto = new ReviewDTO();
        dto.rating = 4;
        dto.comment = "First review";
        dto.userId = testUser.id;
        dto.bookId = testBook.id;

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("/api/reviews")
        .then()
            .statusCode(409)
            .body(containsString("User has already reviewed this book"));
    }
} 