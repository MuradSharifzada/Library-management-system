package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping()
    public ResponseEntity<String> createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        authorService.createAuthor(authorRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Author created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable(name = "id") Long id) {
        AuthorResponse authorResponse = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable(name = "id") Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable(name = "id") Long id,
                                               @Valid @RequestBody AuthorRequest authorRequest) {
        authorService.updateAuthor(id, authorRequest);
        return ResponseEntity.ok("Author updated successfully");
    }
}
