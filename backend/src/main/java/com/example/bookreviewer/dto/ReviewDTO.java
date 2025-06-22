package com.example.bookreviewer.dto;

import jakarta.validation.constraints.*;

public class ReviewDTO {
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Minimum rating is 1")
    @Max(value = 5, message = "Maximum rating is 5")
    public Integer rating;
    
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    public String comment;
    
    @NotNull(message = "User ID is required")
    public Long userId;
    
    @NotNull(message = "Book ID is required")
    public Long bookId;
} 