package com.example.bookreviewer.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserEntityTest {

    @Test
    @Transactional
    public void testPersistAndFindUser() {
        User user = new User();
        user.username = "unituser";
        user.email = "unituser@example.com";
        user.persist();

        User found = User.find("username", "unituser").firstResult();
        assertNotNull(found);
        assertEquals("unituser", found.username);
        assertEquals("unituser@example.com", found.email);
    }
} 