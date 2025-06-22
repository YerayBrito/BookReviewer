package com.example.bookreviewer.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ReviewEntityTest {

    @Test
    @Transactional
    public void testPersistAndFindReview() {
        // Crear usuario y libro
        User user = new User();
        user.username = "reviewuser";
        user.email = "reviewuser@example.com";
        user.persist();

        Book book = new Book();
        book.title = "Review Book";
        book.author = "Review Author";
        book.persist();

        // Crear review
        Review review = new Review();
        review.rating = 4;
        review.comment = "Very good!";
        review.user = user;
        review.book = book;
        review.persist();

        Review found = Review.find("user = ?1 and book = ?2", user, book).firstResult();
        assertNotNull(found);
        assertEquals(4, found.rating);
        assertEquals("Very good!", found.comment);
        assertEquals(user.id, found.user.id);
        assertEquals(book.id, found.book.id);
    }
} 