package com.librarymanagementsystem.service;


import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Book;

import java.util.List;

public interface AuthorService {

    void createAuthor(AuthorRequest request);

    List<AuthorResponse> getAllAuthors(int pageNumber,int pageSize);

    AuthorResponse getAuthorById(Long id);

    void updateAuthor(Long id, AuthorRequest request);

    void deleteAuthorById(Long id);

    List<Book> getBooksByAuthorId(Long authorId);


}
