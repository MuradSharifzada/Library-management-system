package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.exception.ImageProcessingException;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.BookMapper;
import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.model.entity.Category;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.AuthorService;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.service.CategoryService;
import com.librarymanagementsystem.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StorageService storageService;
    private final CategoryService categoryService;
    private final AuthorService authorService;


    @Override
    public Page<BookResponse> getAllBooks(int pageNumber, int size) {
        log.info("Trying to retrieve all books with page number: {}", pageNumber);

        Pageable pageable = PageRequest.of( pageNumber, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Book> books = bookRepository.findAll(pageable);

        if (books.isEmpty()) {
            String errorMessage = "No books available to retrieve. The library is empty.";
            log.error("Error occurred while retrieving books: {}", errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }

        log.info("Successfully retrieved all books with page number: {}", pageNumber);
        return books.map(bookMapper::entityToResponse);

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
        try {
            deleteBookImage(book.getId());
        } catch (IOException e) {
            throw new ImageProcessingException("Not deleted Book image with id:" + book.getId());
        }

        bookRepository.deleteById(id);
        log.info("Successfully Deleted book with id: {}", id);

    }

    @Override
    public void updateBook(Long id, BookRequest request, MultipartFile file) {
        log.info("Trying to update book with id: {}", id);

        Book existingBook = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book not found for update with id: {}", id);
            return new ResourceNotFoundException("Book not found for update with id: " + id);
        });
        bookMapper.updateEntityFromRequest(request, existingBook);

        if (request.getCategoryId() != null) {
            CategoryResponse categoryResponse = categoryService.getCategoryById(request.getCategoryId());
            Category category = new Category();
            category.setId(categoryResponse.getId());
            existingBook.setCategory(category);
        }
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            List<Author> authors = authorService.getauthorsbyids(request.getAuthorIds());
            existingBook.setAuthors(authors);
        }
        if (file != null && !file.isEmpty()) {
            try {
                String newFileName = storageService.uploadFile(file);
                String oldImage = existingBook.getBookImage();
                existingBook.setBookImage(newFileName);
                if (oldImage != null) {
                    storageService.deleteFile(oldImage);
                }
            } catch (IOException e) {
                log.error("Error uploading book image: {}", e.getMessage());
                throw new RuntimeException("Error uploading book image.");
            }
        } else {
            log.info("No new image uploaded. Keeping existing image.");
        }

        bookRepository.save(existingBook);
        log.info("Book updated successfully with name: {}", request.getName());
    }



    @Override
    public void uploadBookImage(Long id, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book Image not found with id: {}", id);
            return new ResourceNotFoundException("Book Image not found with id: " + id);
        });

        if (file == null || file.isEmpty()) {
            log.error("Failed to upload image file is empty or null for book ID: {}", id);
            throw new IOException("Cannot upload empty or null file.");
        }
        try {
            String fileName = storageService.uploadFile(file);
            log.info("Uploaded file to S3 with name: {} for book ID: {}", fileName, id);

            book.setBookImage(fileName);
            bookRepository.save(book);
            log.info("Successfully updated and saved book ID: {} with image: {}", id, fileName);

        } catch (IOException | ResourceNotFoundException e) {
            log.error("Error uploading book image for book ID: {}, message: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error uploading book image for book ID: {}, message: {}", id, e.getMessage());
            throw new IOException("Unexpected error uploading book image for book ID: " + id, e);
        }
    }

    @Override
    public byte[] downloadBookImage(Long id) throws IOException {

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Book not found with id: {} for downloading", id);
            return new ResourceNotFoundException("Book not found with id: {} for downloading " + id);
        });
        if (book.getBookImage() == null || book.getBookImage().isEmpty()) {
            throw new ResourceNotFoundException("Image not found for book ID: " + book.getId());
        }
        try {
            log.info("Looking for image name in bucket: {}", book.getBookImage());
            byte[] image = storageService.downloadFile(book.getBookImage());
            log.info("Book retrieved successfully");
            return image;

        } catch (IOException | ResourceNotFoundException e) {
            log.error("Error happen downloading book image for book ID: {}, message: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error downloading book image for book ID: {}, message: {}", id, e.getMessage());
            throw new IOException("Unexpected error downloading book image for book ID: " + id, e);
        }
    }

    @Override
    public void deleteBookImage(Long id) throws IOException {

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.info("Deleting failed Image not found with id: {}", id);
            return new ResourceNotFoundException("Deleting failed Image not found with id: " + id);
        });
        try {
            log.info("Deleting image from storage for book ID: {}", id);
            storageService.deleteFile(book.getBookImage());

            book.setBookImage(null);
            bookRepository.save(book);

        } catch (IOException | ResourceNotFoundException e) {
            log.error("Error happen delete file for book ID: {}, message: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting book image for book ID: {}, message: {}", id, e.getMessage());
            throw new IOException("Unexpected error deleting book image for book ID: " + id, e);
        }
    }

    @Override
    public Long countBooks() {
        return bookRepository.count();
    }

    @Override
    public Page<BookResponse> getFilteredBooks(String category, String bookName, int page, int size) {
        log.info("Fetching filtered books with category: {} and bookName: {}", category, bookName);

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books;

        if ((category != null && !category.isEmpty()) && (bookName != null && !bookName.isEmpty())) {

            books = bookRepository.findBooksByCategory(category, pageable);

            List<Book> filteredBooks = books.getContent().stream()
                    .filter(book -> book.getName().toLowerCase().contains(bookName.toLowerCase()))
                    .toList();

            books = new PageImpl<>(filteredBooks, pageable, filteredBooks.size());
        } else if (category != null && !category.isEmpty()) {
            books = bookRepository.findBooksByCategory(category, pageable);
        } else if (bookName != null && !bookName.isEmpty()) {
            books = bookRepository.findByName(bookName, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }

        return books.map(bookMapper::entityToResponse);
    }

    @Override
    public void createBook(BookRequest request, MultipartFile file) {
        log.info("Trying to create a new book with name: {}", request.getName());

        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new ResourceAlreadyExistsException("Book ISBN already exists: " + request.getIsbn());
        }

        Book book = bookMapper.requestToEntity(request);

        CategoryResponse categoryResponse = categoryService.getCategoryById(request.getCategoryId());
        Category category = new Category();
        category.setId(categoryResponse.getId());
        book.setCategory(category);

        List<Author> authors = authorService.getauthorsbyids(request.getAuthorIds());
        book.setAuthors(authors);

        book = bookRepository.save(book);

        if (file != null && !file.isEmpty()) {
            try {
                uploadBookImage(book.getId(), file);

            } catch (IOException e) {
                log.error("Error occurred while uploading image: {}", e.getMessage());
                throw new ImageProcessingException("Image not uploaded for book id:" + book.getId());

            }
        }
        log.info("Successfully created new book with name: {}", request.getName());
    }



}
