package com.example.bookreviewer.resource;

import com.example.bookreviewer.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserResourceTest {

    @Test
    public void testGetAllUsers() {
        given()
          .when().get("/api/users")
          .then()
             .statusCode(200)
             .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.username = "newuser";
        user.email = "newuser@example.com";

        given()
          .contentType(ContentType.JSON)
          .body(user)
        .when()
          .post("/api/users")
        .then()
          .statusCode(201)
          .body("username", equalTo("newuser"))
          .body("email", equalTo("newuser@example.com"));
    }

    @Test
    public void testUpdateUser() {
        User newUser = new User();
        newUser.username = "originaluser";
        newUser.email = "original@example.com";

        Long userId = given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        User updatedUser = new User();
        updatedUser.username = "updateduser";
        updatedUser.email = "updated@example.com";

        given()
            .contentType(ContentType.JSON)
            .body(updatedUser)
        .when()
            .put("/api/users/" + userId)
        .then()
            .statusCode(200)
            .body("username", equalTo("updateduser"))
            .body("email", equalTo("updated@example.com"))
            .body("id", equalTo(userId.intValue()));
    }

    @Test
    public void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.username = "updateduser";
        updatedUser.email = "updated@example.com";

        given()
            .contentType(ContentType.JSON)
            .body(updatedUser)
        .when()
            .put("/api/users/9999")
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testDeleteUser() {
        User newUser = new User();
        newUser.username = "usertodelete";
        newUser.email = "delete@example.com";

        Long userId = given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .delete("/api/users/" + userId)
        .then()
            .statusCode(204);

        given()
        .when()
            .get("/api/users/" + userId)
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testDeleteUserNotFound() {
        given()
        .when()
            .delete("/api/users/9999")
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testGetUserById() {
        User newUser = new User();
        newUser.username = "testuser";
        newUser.email = "test@example.com";

        Long userId = given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
        .when()
            .get("/api/users/" + userId)
        .then()
            .statusCode(200)
            .body("username", equalTo("testuser"))
            .body("email", equalTo("test@example.com"))
            .body("id", equalTo(userId.intValue()));
    }

    @Test
    public void testGetUserByIdNotFound() {
        given()
        .when()
            .get("/api/users/9999")
        .then()
            .statusCode(404)
            .body(containsString("User not found"));
    }

    @Test
    public void testCreateUserDuplicateUsername() {
        User user1 = new User();
        user1.username = "duplicateuser";
        user1.email = "user1@example.com";

        User user2 = new User();
        user2.username = "duplicateuser";
        user2.email = "user2@example.com";

        given()
            .contentType(ContentType.JSON)
            .body(user1)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body(user2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(409)
            .body(containsString("User already exists with username"));
    }

    @Test
    public void testCreateUserDuplicateEmail() {
        User user1 = new User();
        user1.username = "user1";
        user1.email = "duplicate@example.com";

        User user2 = new User();
        user2.username = "user2";
        user2.email = "duplicate@example.com";

        given()
            .contentType(ContentType.JSON)
            .body(user1)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body(user2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(409)
            .body(containsString("User already exists with email"));
    }
} 