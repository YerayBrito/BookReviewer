package com.example.bookreviewer.resource;

import com.example.bookreviewer.dto.UserDTO;
import com.example.bookreviewer.entity.Review;
import com.example.bookreviewer.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public Response getAllUsers() {
        try {
            List<User> users = User.listAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Response.ok(userDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving users: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            User user = User.findById(id);
            if (user != null) {
                UserDTO userDTO = convertToDTO(user);
                return Response.ok(userDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user: " + e.getMessage())
                    .build();
        }
    }
    
    @POST
    @Transactional
    public Response createUser(@Valid User user) {
        try {
            User existingUser = User.find("username", user.username).firstResult();
            if (existingUser != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User already exists with username: " + user.username)
                        .build();
            }

            existingUser = User.find("email", user.email).firstResult();
            if (existingUser != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User already exists with email: " + user.email)
                        .build();
            }

            user.persist();
            if (user.isPersistent()) {
                UserDTO userDTO = convertToDTO(user);
                return Response.status(Response.Status.CREATED).entity(userDTO).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Error creating user")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error creating user: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, @Valid User userUpdate) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id)
                        .build();
            }

            User existingUser = User.find("username = ?1 and id != ?2", userUpdate.username, id).firstResult();
            if (existingUser != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User already exists with username: " + userUpdate.username)
                        .build();
            }

            existingUser = User.find("email = ?1 and id != ?2", userUpdate.email, id).firstResult();
            if (existingUser != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User already exists with email: " + userUpdate.email)
                        .build();
            }

            user.username = userUpdate.username;
            user.email = userUpdate.email;
            user.updatedAt = java.time.LocalDateTime.now();

            UserDTO userDTO = convertToDTO(user);
            return Response.ok(userDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating user: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id)
                        .build();
            }

            user.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting user: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}/reviews")
    public Response getUserReviews(@PathParam("id") Long id) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id)
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

    @GET
    @Path("/search")
    public Response searchUsers(@QueryParam("username") String username, @QueryParam("email") String email) {
        try {
            List<User> users;
            if (username != null && !username.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
                users = User.list("username like ?1 and email like ?2", "%" + username + "%", "%" + email + "%");
            } else if (username != null && !username.trim().isEmpty()) {
                users = User.list("username like ?1", "%" + username + "%");
            } else if (email != null && !email.trim().isEmpty()) {
                users = User.list("email like ?1", "%" + email + "%");
            } else {
                users = User.listAll();
            }

            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Response.ok(userDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching users: " + e.getMessage())
                    .build();
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.id;
        dto.username = user.username;
        dto.email = user.email;
        dto.createdAt = user.createdAt;
        dto.updatedAt = user.updatedAt;
        dto.averageRating = user.getAverageRating();
        dto.reviewsCount = user.getReviewsCount();

        if (user.reviews != null && !user.reviews.isEmpty()) {
            dto.reviews = user.reviews.stream()
                    .map(this::convertToUserReviewDTO)
                    .collect(Collectors.toList());
        }

        return dto;
    }

    private UserDTO.UserReviewDTO convertToUserReviewDTO(Review review) {
        UserDTO.UserReviewDTO dto = new UserDTO.UserReviewDTO();
        dto.id = review.id;
        dto.rating = review.rating;
        dto.comment = review.comment;
        dto.createdAt = review.createdAt;
        dto.bookTitle = review.book != null ? review.book.title : "Unknown book";
        dto.bookAuthor = review.book != null ? review.book.author : "Unknown author";
        return dto;
    }
} 