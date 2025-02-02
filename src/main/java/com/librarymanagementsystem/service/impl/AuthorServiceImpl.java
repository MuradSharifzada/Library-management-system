package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.AuthorMapper;
import com.librarymanagementsystem.mapper.BookMapper;
import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.repository.AuthorRepository;
import com.librarymanagementsystem.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @Override
    public void createAuthor(AuthorRequest request) {

        if (authorRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            log.error("Author {} {} already exists in Database", request.getFirstName(), request.getLastName());
            throw new ResourceAlreadyExistsException("Author " + request.getFirstName() + " " + request.getLastName() + " already exists");
        }
        authorRepository.save(authorMapper.requestToEntity(request));
        log.info("Successfully created new Author: {} {}", request.getFirstName(), request.getLastName());
    }

    @Override
    public Page<AuthorResponse> getAllAuthors(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Author> authors = authorRepository.findAll(pageable);
        if (authors.isEmpty()) {
            log.warn("No authors found in the database");
            throw new ResourceNotFoundException("No authors found");
        }
        return authors.map(authorMapper::entityToResponse);
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {

        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author retrieval failed - ID: {} not found", id);
            return new ResourceNotFoundException("Author not found with ID: " + id);
        });
        return authorMapper.entityToResponse(author);
    }

    @Override
    public void updateAuthor(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for updating", id);
            return new ResourceNotFoundException("Author not found with ID: " + id);
        });

        Author updatedAuthor = authorMapper.requestToEntity(request);
        updatedAuthor.setId(author.getId());
        authorRepository.save(updatedAuthor);
        log.info("Author updated successfully");
    }

    @Override
    public void deleteAuthorById(Long id) {

        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for deletion", id);
            return new ResourceNotFoundException("Author not found with ID: " + id);
        });
        authorRepository.deleteById(author.getId());
        log.info("Author deleted successfully");
    }

    @Override
    public List<BookResponse> getBooksByAuthorId(Long id) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        if (author.getBooks().isEmpty()) {
            throw new ResourceNotFoundException("No books found for author ID: " + id);
        }

        return author.getBooks().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<Author> getAuthorsById(List<Long> authorIds) {
        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No valid authors found for the given IDs.");
        }
        return authors;
    }

    @Override
    public Long countAuthors() {
        return authorRepository.count();
    }

}
