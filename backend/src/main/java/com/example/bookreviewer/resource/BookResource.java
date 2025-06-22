package com.example.bookreviewer.resource;

import com.example.bookreviewer.dto.BookDTO;
import com.example.bookreviewer.entity.Book;
import com.example.bookreviewer.entity.Review;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @GET
    public Response getAllBooks() {
        try {
            List<Book> books = Book.listAll();
            List<BookDTO> bookDTOs = books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Response.ok(bookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving books: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        try {
            Book book = Book.findById(id);
            if (book != null) {
                BookDTO bookDTO = convertToDTO(book);
                return Response.ok(bookDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving book: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Transactional
    public Response createBook(@Valid Book book) {
        try {
            book.persist();
            if (book.isPersistent()) {
                BookDTO bookDTO = convertToDTO(book);
                return Response.status(Response.Status.CREATED).entity(bookDTO).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Error creating book")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error creating book: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBook(@PathParam("id") Long id, @Valid Book bookUpdate) {
        try {
            Book book = Book.findById(id);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id)
                        .build();
            }

            book.title = bookUpdate.title;
            book.author = bookUpdate.author;
            book.updatedAt = java.time.LocalDateTime.now();

            BookDTO bookDTO = convertToDTO(book);
            return Response.ok(bookDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating book: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBook(@PathParam("id") Long id) {
        try {
            Book book = Book.findById(id);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id)
                        .build();
            }

            book.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting book: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchBooks(@QueryParam("title") String title, @QueryParam("author") String author) {
        try {
            List<Book> books;
            if (title != null && !title.trim().isEmpty() && author != null && !author.trim().isEmpty()) {
                books = Book.list("title like ?1 and author like ?2", "%" + title + "%", "%" + author + "%");
            } else if (title != null && !title.trim().isEmpty()) {
                books = Book.list("title like ?1", "%" + title + "%");
            } else if (author != null && !author.trim().isEmpty()) {
                books = Book.list("author like ?1", "%" + author + "%");
            } else {
                books = Book.listAll();
            }

            List<BookDTO> bookDTOs = books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Response.ok(bookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching books: " + e.getMessage())
                    .build();
        }
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.id = book.id;
        dto.title = book.title;
        dto.author = book.author;
        dto.createdAt = book.createdAt;
        dto.updatedAt = book.updatedAt;
        dto.averageRating = book.getAverageRating();
        dto.reviewsCount = book.getReviewsCount();

        if (book.reviews != null && !book.reviews.isEmpty()) {
            dto.reviews = book.reviews.stream()
                    .map(this::convertToReviewSummaryDTO)
                    .collect(Collectors.toList());
        }

        return dto;
    }

    private BookDTO.ReviewSummaryDTO convertToReviewSummaryDTO(Review review) {
        BookDTO.ReviewSummaryDTO dto = new BookDTO.ReviewSummaryDTO();
        dto.id = review.id;
        dto.rating = review.rating;
        dto.comment = review.comment;
        dto.createdAt = review.createdAt;
        dto.username = review.user != null ? review.user.username : "Anonymous";
        return dto;
    }
} 