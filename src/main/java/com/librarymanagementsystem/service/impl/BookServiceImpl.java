package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.BookMapper;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public void createBook(BookRequest request) {
        log.info("Trying to create a new book with name: {}", request.getName());

        bookRepository
                .findByIsbn(request.getIsbn())
                .ifPresent(existingBook -> {
                    log.error("ISBN {} already exists in the database", request.getIsbn());
                    throw new ResourceAlreadyExistsException("Book ISBN already exists: " + request.getIsbn());
                });
        bookRepository.save(bookMapper.requestToEntity(request));
        log.info("Successfully created new book with name: {}", request.getName());

    }

    @Override
    public List<BookResponse> getAllBooks(int pageNumber, int size) {
        log.info("Trying to retrieve all books with page number: {}", pageNumber);

        Pageable pageable = PageRequest.of(pageNumber, size);

        Page<Book> books = bookRepository.findAll(pageable);

        if (books.isEmpty()) {
            String errorMessage = "No books available to retrieve. The library is empty.";
            log.error("Error occurred while retrieving books: {}", errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }


        log.info("Successfully retrieved all books with page number: {}", pageNumber);
        return books.stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(Long id) {
        log.info("Trying to find book with: {}", id);

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book not exist with id: {}", id);
            return new ResourceNotFoundException("book not exist with id: " + id);
        });
        log.info("Successfully retrieved Book with id: {} ", id);
        return bookMapper.entityToResponse(book);
    }

    @Override
    public void deleteBookById(Long id) {
        log.info("Trying to delete book with id: {}", id);

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book not exist for deleting with id: {}", id);
            return new ResourceNotFoundException("book not exist for deleting with id: " + id);
        });
        bookRepository.deleteById(id);
        log.info("Successfully Deleted book with id: {}", id);

    }

    @Override
    public void updateBook(Long id, BookRequest request) {
        log.info("Trying to update book with id: {}", id);


        Book existingBook = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book not found for update with id: {}", id);
            return new ResourceNotFoundException("Book not found for update with id: " + id);
        });
        Book updatedBook = bookMapper.requestToEntity(request);
        updatedBook.setId(existingBook.getId());

        bookRepository.save(updatedBook);
        log.info("Book updated successfully with name: {}", request.getName());

    }

    @Override
    public List<BookResponse> findByBookName(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (pageSize < 0) {
            throw new IllegalArgumentException("Page must be positive or not equal to " + pageNumber);
        }

        log.info("Retrieved books by name");
        return bookRepository.findByName(name, pageable)
                .stream().map(bookMapper::entityToResponse).collect(Collectors.toList());
    }


    @Override
    public List<BookResponse> findBooksByCategory(String name, int pageNumber, int pageSize) {
        log.info("Trying to find books by category name: {}", name);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<BookResponse> books = bookRepository.findBooksByCategory(name, pageable)
                .stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());

        log.info("Successfully retrieved {} books for category name: {}", books.size(), name);
        return books;
    }
}
