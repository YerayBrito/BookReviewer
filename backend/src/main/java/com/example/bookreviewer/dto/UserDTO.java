package com.example.bookreviewer.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {
    public Long id;
    public String username;
    public String email;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public double averageRating;
    public int reviewsCount;
    public List<UserReviewDTO> reviews;
    
    public static class UserReviewDTO {
        public Long id;
        public Integer rating;
        public String comment;
        public LocalDateTime createdAt;
        public String bookTitle;
        public String bookAuthor;
    }
} 