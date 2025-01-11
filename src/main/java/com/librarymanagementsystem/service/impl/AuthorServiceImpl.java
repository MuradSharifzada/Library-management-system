package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.AuthorMapper;
import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.repository.AuthorRepository;
import com.librarymanagementsystem.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    @Override
    public void createAuthor(AuthorRequest request) {
        log.info("Attempting to create new Author with name: " + request.getFirstName());
        if (authorRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            log.error("Author {} and {} already exists in Database", request.getFirstName(), request.getLastName());
            throw new ResourceAlreadyExistsException("Author " + request.getFirstName() + " and " + request.getLastName() + " already exist");
        }
        authorRepository.save(authorMapper.requestToEntity(request));
        log.info("Successfully created new Author with: " + request.getFirstName() + " " + request.getLastName());
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        log.info("Attempting to retrieve all authors");
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            log.warn("No authors found in the database");
            throw new ResourceNotFoundException("No authors found");
        } else {
            log.info("Successfully retrieved {} authors", authors.size());
        }
        return authorRepository
                .findAll()
                .stream()
                .map(authorMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        log.info("Attempting to retrieve author with ID: {}", id);

        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author retrieval failed - author with ID: {} not found", id);
            throw  new  ResourceNotFoundException("Author not found with ID: " + id);
        });

        log.info("Successfully retrieved author with ID: {}", id);

        return authorMapper.entityToResponse(author);
    }

    @Override
    public void updateAuthor(Long id, AuthorRequest request) {
        log.info("Attempting to update author with ID: {}", id);

        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for updating", id);
            throw  new ResourceNotFoundException("Author not found with ID: " + id);
        });

        Author newAuthor = authorMapper.requestToEntity(request);
        newAuthor.setId(author.getId());
        authorRepository.save(newAuthor);
        log.info("Author updated successfully");
    }

    @Override
    public void deleteAuthorById(Long id) {
        log.info("Attempting to delete author with ID: {}", id);

        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for deleting", id);
            throw  new ResourceNotFoundException("Author not found with ID: " + id);
        });
        authorRepository.deleteById(id);
        log.info("Author deleted with {}", id);
    }

    @Override
    public List<Book> getBooksByAuthorId(Long id) {

        log.info("Attempting to retrieve author books  with ID: {}", id);

        Author author = authorRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        log.info("Getting books by author id.ID: {} Book count: {}", id, author.getBooks().size());
        return new ArrayList<>(author.getBooks());
    }
}
