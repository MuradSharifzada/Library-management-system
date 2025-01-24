package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.AuthorMapper;
import com.librarymanagementsystem.mapper.BookMapper;
import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.repository.AuthorRepository;
import com.librarymanagementsystem.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

   // @CachePut(value = "authors", keyGenerator = "customKeyGenerator",unless ="#result==null")
    @Override
    public void createAuthor(AuthorRequest request) {
        log.info("Attempting to create new Author with name: {}", request.getFirstName());
        if (authorRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            log.error("Author {} {} already exists in Database", request.getFirstName(), request.getLastName());
            throw new ResourceAlreadyExistsException("Author " + request.getFirstName() + " " + request.getLastName() + " already exists");
        }
        authorRepository.save(authorMapper.requestToEntity(request));
        log.info("Successfully created new Author: {} {}", request.getFirstName(), request.getLastName());
    }

   // @Cacheable(value = "authors", keyGenerator = "customKeyGenerator", unless = "#result.isEmpty()")
    @Override
    public List<AuthorResponse> getAllAuthors(int pageNumber, int pageSize) {
        log.info("Attempting to retrieve all authors");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Author> authors = authorRepository.findAll(pageable);
        if (authors.isEmpty()) {
            log.warn("No authors found in the database");
            throw new ResourceNotFoundException("No authors found");
        }
        return authors.getContent().stream()
                .map(authorMapper::entityToResponse)
                .collect(Collectors.toList());
    }

   // @Cacheable(value = "author", keyGenerator = "customKeyGenerator", unless = "#result == null")
    @Override
    public AuthorResponse getAuthorById(Long id) {
        log.info("Retrieving author with ID: {}", id);
        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author retrieval failed - ID: {} not found", id);
            throw new ResourceNotFoundException("Author not found with ID: " + id);
        });
        return authorMapper.entityToResponse(author);
    }

//    @Caching(
//            evict = {@CacheEvict(value = "authors", keyGenerator = "customKeyGenerator")},
//            put = {@CachePut(value = "authors", keyGenerator = "customKeyGenerator")}
//    )
    @Override
    public void updateAuthor(Long id, AuthorRequest request) {
        log.info("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for updating", id);
            return new ResourceNotFoundException("Author not found with ID: " + id);
        });

        Author updatedAuthor = authorMapper.requestToEntity(request);
        updatedAuthor.setId(author.getId());
        authorRepository.save(updatedAuthor);
        log.info("Author updated successfully");
    }

   // @CacheEvict(value = "authors", keyGenerator = "customKeyGenerator")
    @Override
    public void deleteAuthorById(Long id) {
        log.info("Deleting author with ID: {}", id);
        Author author = authorRepository.findById(id).orElseThrow(() -> {
            log.error("Author with ID: {} not found for deletion", id);
            return new ResourceNotFoundException("Author not found with ID: " + id);
        });
        authorRepository.deleteById(author.getId());
        log.info("Author deleted successfully");
    }

   // @Cacheable(value = "booksByAuthor", keyGenerator = "customKeyGenerator", unless = "#result.isEmpty()")
    @Override
    public List<BookResponse> getBooksByAuthorId(Long id) {
        log.info("Retrieving books by author ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        if (author.getBooks().isEmpty()) {
            throw new ResourceNotFoundException("No books found for author ID: " + id);
        }

        return author.getBooks().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
