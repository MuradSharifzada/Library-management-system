package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void createBook(BookRequest request,MultipartFile file);

    List<BookResponse> getAllBooks(int pageNumber, int size);

    BookResponse getBookById(Long id);

    void deleteBookById(Long id);

    void updateBook(Long id, BookRequest request,MultipartFile file);


    void uploadBookImage(Long id, MultipartFile file) throws IOException;

    byte[] downloadBookImage(Long id) throws IOException;

    void deleteBookImage(Long id) throws IOException;

    Long countBooks();

    Page<BookResponse> getFilteredBooks(String category, String name, int page, int size);
}
