package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    void createBook(BookRequest request);

    List<BookResponse> getAllBooks(int pageNumber, int size);

    BookResponse getBookById(Long id);

    void deleteBookById(Long id);

    void updateBook(Long id, BookRequest request);

    List<BookResponse> findByBookName(String name, int pageNumber, int size);

    List<BookResponse> findBooksByCategory(String name, int pageNumber, int pageSize);



}
