package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.dto.response.ErrorResponse;
import com.librarymanagementsystem.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Create a new Author", description = "Creates a new author in the system based on the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<String> createAuthor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the author to create",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorRequest.class),
                            examples = @ExampleObject(value = "{ \"firstName\": \"James\", \"lastName\": \"Clear\", \"birthDay\": \"1986-01-22\", \"biography\": \"Author biography.\" }")))
            @Valid @RequestBody AuthorRequest authorRequest) {
        authorService.createAuthor(authorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Author created successfully");
    }

    @Operation(summary = "Get all Authors", description = "Retrieves a paginated list of all authors in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors(
            @Parameter(description = "Page number for pagination, defaults to 0", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 10", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageNumber, pageSize));
    }

    @Operation(summary = "Get Author by ID", description = "Retrieves the details of a specific author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author details retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(description = "ID of the author to retrieve") @PathVariable Long id) {
        AuthorResponse authorResponse = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorResponse);
    }

    @Operation(summary = "Delete Author by ID", description = "Deletes an author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted successfully",
                    content = @Content(
                            schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(
            @Parameter(description = "ID of the author to delete") @PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @Operation(summary = "Update Author", description = "Updates the details of an existing author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorRequest> updateAuthor(
            @Parameter(description = "ID of the author to update") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the author",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorRequest.class),
                            examples = @ExampleObject(value = "{ \"firstName\": \"James\", \"lastName\": \"Smith\", \"birthDay\": \"1990-05-15\", \"biography\": \"Updated biography\" }")))
            @Valid @RequestBody AuthorRequest authorRequest) {
        authorService.updateAuthor(id, authorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authorRequest);
    }

    @Operation(summary = "Find Books by Author ID", description = "Retrieves a list of books written by a specific author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/books/{id}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthorId(
            @Parameter(description = "ID of the author to retrieve books for") @PathVariable Long id) {
        return ResponseEntity.ok(authorService.getBooksByAuthorId(id));
    }
}
