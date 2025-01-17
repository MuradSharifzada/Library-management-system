package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
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

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;


    @Operation(summary = "Create new Author", description = "Creates a new author system based on the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author Created Successfully",
                    content = @Content(schema = @Schema(implementation = AuthorRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = AuthorRequest.class)))
    })
    @PostMapping
    public ResponseEntity<String> createAuthor(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Author to create", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthorRequest.class),
                    examples = @ExampleObject(value = "{ \"firstName\": \"James\", \"lastName\": \"Clear\",\"birthDay\": \"1986-01-22\",\"biography\":\"its too long\" }")))
                                               @Valid @RequestBody AuthorRequest authorRequest

    ) {
        authorService.createAuthor(authorRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Author created successfully");
    }

    @Operation(summary = "Get all Authors", description = "Retrieves a list of all authors in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Authors found")
    })
    @GetMapping
    public ResponseEntity<?> getAllAuthors(int pageNumber,int pageSize) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageNumber,pageSize));
    }

    @Operation(summary = "Get Author by ID", description = "Retrieves the details of a specific author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author details retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@Parameter(description = "id of author to be searched") @PathVariable(name = "id") Long id) {
        AuthorResponse authorResponse = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorResponse);
    }

    @Operation(summary = "Delete Author by ID", description = "Deletes an author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable(name = "id") Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @Operation(summary = "Update Author", description = "Updates the details of an existing author by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuthor(@Parameter(description = "ID of the author to be updated") @PathVariable(name = "id") Long id,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "Updated author details", required = true,
                                                       content = @Content(mediaType = "application/json",
                                                               schema = @Schema(implementation = AuthorRequest.class),
                                                               examples = @ExampleObject(value = "{ \"firstName\": \"James\", \"lastName\": \"Smith\", \"birthDay\": \"1990-05-15\", \"biography\": \"Updated biography\" }")))
                                               @Valid @RequestBody AuthorRequest authorRequest) {
        authorService.updateAuthor(id, authorRequest);
        return ResponseEntity.ok("Author updated successfully");
    }
}
