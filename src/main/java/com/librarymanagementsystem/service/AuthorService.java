package com.librarymanagementsystem.service;


import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.model.entity.Author;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    void createAuthor(AuthorRequest request);

    Page<AuthorResponse> getAllAuthors(int pageNumber, int pageSize);

    AuthorResponse getAuthorById(Long id);

    void updateAuthor(Long id, AuthorRequest request);

    void deleteAuthorById(Long id);

    List<BookResponse> getBooksByAuthorId(Long authorId);

    List<Author> getauthorsbyids(List<Long> authorIds);

    Long countAuthors();
}
