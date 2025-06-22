package com.example.bookreviewer.resource;

import com.example.bookreviewer.dto.ReviewDTO;
import com.example.bookreviewer.entity.Book;
import com.example.bookreviewer.entity.Review;
import com.example.bookreviewer.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @POST
    @Transactional
    public Response addReview(@Valid ReviewDTO reviewDTO) {
        try {
            User user = User.findById(reviewDTO.userId);
            Book book = Book.findById(reviewDTO.bookId);

            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + reviewDTO.userId)
                        .build();
            }
            
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + reviewDTO.bookId)
                        .build();
            }
            
            long existingReviews = Review.count("user = ?1 and book = ?2", user, book);
            if (existingReviews > 0) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User has already reviewed this book")
                        .build();
            }

            Review review = new Review();
            review.user = user;
            review.book = book;
            review.rating = reviewDTO.rating;
            review.comment = reviewDTO.comment;

            review.persist();

            return Response.status(Response.Status.CREATED).entity(review).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error creating review: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllReviews() {
        try {
            List<Review> reviews = Review.listAll();
            return Response.ok(reviews).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving reviews: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getReviewById(@PathParam("id") Long id) {
        try {
            Review review = Review.findById(id);
            if (review != null) {
                return Response.ok(review).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Review not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving review: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/book/{bookId}")
    public Response getReviewsByBook(@PathParam("bookId") Long bookId) {
        try {
            Book book = Book.findById(bookId);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + bookId)
                        .build();
            }

            List<Review> reviews = Review.list("book = ?1", book);
            return Response.ok(reviews).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving book reviews: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getReviewsByUser(@PathParam("userId") Long userId) {
        try {
            User user = User.findById(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + userId)
                        .build();
            }

            List<Review> reviews = Review.list("user = ?1", user);
            return Response.ok(reviews).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user reviews: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateReview(@PathParam("id") Long id, @Valid ReviewDTO reviewDTO) {
        try {
            Review review = Review.findById(id);
            if (review == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Review not found with ID: " + id)
                        .build();
            }

            review.rating = reviewDTO.rating;
            review.comment = reviewDTO.comment;
            review.updatedAt = java.time.LocalDateTime.now();

            return Response.ok(review).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating review: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteReview(@PathParam("id") Long id) {
        try {
            Review review = Review.findById(id);
            if (review == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Review not found with ID: " + id)
                        .build();
            }

            review.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting review: " + e.getMessage())
                    .build();
        }
    }
} 