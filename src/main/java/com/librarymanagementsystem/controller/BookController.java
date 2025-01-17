package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @PostMapping
    public ResponseEntity<String> createBook(
            @RequestBody @Valid BookRequest bookRequest
    ) {
        bookService.createBook(bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Book created successfully.");
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int size) {
        List<BookResponse> books = bookService.getAllBooks(pageNumber, size);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse bookResponse = bookService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        bookService.updateBook(id, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> findByBookName(
            @RequestParam(name = "Search Book Name") String name,
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "5", required = false) int size) {

        List<BookResponse> books = bookService.findByBookName(name, pageNumber, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(books);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<BookResponse>> findBooksByCategory(
            @RequestParam(name = "Search Category Name") String name,
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "5", required = false) int pageSize) {

        List<BookResponse> books = bookService.findBooksByCategory(name, pageNumber, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(books);

    }

    @Operation(summary = "Upload an image for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBookImage(
            @Parameter(description = "Book ID to which the image will be uploaded") @PathVariable Long id,
            @Parameter(description = "The image file to upload", required = true)
            @RequestParam("file") MultipartFile file) throws IOException{

        bookService.uploadBookImage(id, file);

        return ResponseEntity.ok("Image uploaded successfully for book ID: " + id);
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> downloadBookImage(@PathVariable Long id) throws IOException {
        byte[] imageData = bookService.downloadBookImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"book-image\"")
                .body(imageData);
    }
    
    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteBookImage(@PathVariable(name = "id") Long id) throws IOException {
        bookService.deleteBookImage(id);
        return ResponseEntity.ok("Image deleted successfully for book ID: " + id);
    }


}
