package com.example.bookreviewer.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BookEntityTest {

    @Test
    @Transactional
    public void testPersistAndFindBook() {
        Book book = new Book();
        book.title = "Unit Test Book";
        book.author = "Unit Tester";
        book.persist();

        Book found = Book.find("title", "Unit Test Book").firstResult();
        assertNotNull(found);
        assertEquals("Unit Test Book", found.title);
        assertEquals("Unit Tester", found.author);
    }
} 