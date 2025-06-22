package com.example.bookreviewer.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookDTO {
    public Long id;
    public String title;
    public String author;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public double averageRating;
    public int reviewsCount;
    public List<ReviewSummaryDTO> reviews;
    
    public static class ReviewSummaryDTO {
        public Long id;
        public Integer rating;
        public String comment;
        public LocalDateTime createdAt;
        public String username;
    }
} 