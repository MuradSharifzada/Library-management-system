package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @Operation(summary = "Create new Book", description = "Creates a new book system based on the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book Created Successfully",
                    content = @Content(schema = @Schema(implementation = BookRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<String> createBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Book to create", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookRequest.class),
                    examples = @ExampleObject(
                            value = "{\"name\": \"Atomic Habits\", \"isbn\": \"9783442178582\", " +
                                    "\"description\": \"Transformative guide to building better habits through small, consistent changes.\", " +
                                    "\"publishedTime\": \"2016-11-16\", \"categoryId\": 1, \"stockCount\": 100, \"authorId\": [1]}")))
                                             @RequestBody @Valid BookRequest bookRequest
    ) {
        bookService.createBook(bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book created successfully.");
    }


    @Operation(summary = "Get All Books", description = "Retrieves a list of all books in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @Parameter(description = "Page number for pagination, defaults to 0", example = "0")
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 10", example = "10")
            @RequestParam(defaultValue = "10", required = false) int size) {
        List<BookResponse> books = bookService.getAllBooks(pageNumber, size);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }


    @Operation(summary = "Get Book by ID", description = "Retrieves a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@Parameter(description = "ID of the book to retrieve") @PathVariable Long id) {
        BookResponse bookResponse = bookService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }


    @Operation(summary = "Delete Book by ID", description = "Deletes a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@Parameter(description = "ID of the book to delete") @PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }


    @Operation(summary = "Update Book", description = "Updates the details of a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@Parameter(description = "ID of the book to update") @PathVariable Long id,
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                     description = "Updated book details", required = true,
                                                     content = @Content(mediaType = "application/json",
                                                             schema = @Schema(implementation = BookRequest.class)))
                                             @Valid @RequestBody BookRequest bookRequest) {
        bookService.updateBook(id, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully.");
    }


    @Operation(summary = "Search Books by Name", description = "Searches for books by their name with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> findByBookName(
            @Parameter(description = "Name of the book to search for")
            @RequestParam(name = "Search Book Name") String name,
            @Parameter(description = "Page number for pagination, defaults to 0")
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 5")
            @RequestParam(defaultValue = "5", required = false) int size) {

        List<BookResponse> books = bookService.findByBookName(name, pageNumber, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(books);
    }

    @Operation(summary = "Search Books by Category", description = "Searches for books by category name with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search/category")
    public ResponseEntity<List<BookResponse>> findBooksByCategory(
            @Parameter(description = "Name of the category to search for")
            @RequestParam(name = "Search Category Name") String name,
            @Parameter(description = "Page number for pagination, defaults to 0")
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 5")
            @RequestParam(defaultValue = "5", required = false) int pageSize) {

        List<BookResponse> books = bookService.findBooksByCategory(name, pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(books);
    }

    @Operation(summary = "Upload an Image for a Book", description = "Uploads an image for a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBookImage(
            @Parameter(description = "Book ID to which the image will be uploaded") @PathVariable Long id,
            @Parameter(description = "The image file to upload", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {

        bookService.uploadBookImage(id, file);
        return ResponseEntity.ok("Image uploaded successfully for book ID: " + id);
    }


    @Operation(summary = "Download a Book Image", description = "Downloads the image of a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image downloaded successfully")
    })
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> downloadBookImage(@Parameter(description = "Book ID of the image to download") @PathVariable Long id) throws IOException {
        byte[] imageData = bookService.downloadBookImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"book-image\"")
                .body(imageData);
    }


    @Operation(summary = "Delete a Book Image", description = "Deletes the image of a specific book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully")
    })
    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteBookImage(@Parameter(description = "Book ID of the image to delete") @PathVariable(name = "id") Long id) throws IOException {
        bookService.deleteBookImage(id);
        return ResponseEntity.ok("Image deleted successfully for book ID: " + id);
    }


}
