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
import com.librarymanagementsystem.service.CategoryService;
import com.librarymanagementsystem.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(value = MockitoExtension.class)
class BookServiceImplTest {


    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private StorageService storageService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookRequest bookRequest;
    private Book book;
    private BookResponse bookResponse;
    private CategoryResponse categoryResponse;
    private MultipartFile file;
    private List<Author> authors;


    @BeforeEach
    void setUp() {
        bookRequest = BookRequest.builder()
                .name("Atomic Habits")
                .isbn("9780735211292")
                .description("A guide to building good habits.")
                .publishedTime(LocalDate.of(2018, 10, 16))
                .categoryId(2L)
                .authorIds(List.of(1L, 2L))
                .bookImage("atomic_habits.jpg")
                .stockCount(10)
                .build();

        book = Book.builder()
                .id(1L)
                .name("Atomic Habits")
                .isbn("9780735211292")
                .description("A guide to building good habits.")
                .publishedTime(LocalDate.of(2018, 10, 16))
                .category(new Category())
                .authors(List.of(new Author(), new Author()))
                .bookImage("atomic_habits.jpg")
                .stockCount(10)
                .build();

        bookResponse = BookResponse.builder()
                .id(1L)
                .name("Atomic Habits")
                .isbn("9780735211292")
                .description("A guide to building good habits.")
                .build();

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(2L);

        authors = List.of(new Author(), new Author());

        file = new MockMultipartFile("file", "atomic_habits.jpg", "image/jpeg", new byte[10]);
    }

    @Test
    void givenCreateBook_WhenBookIsbnIsUnique_ThenCreateNewBook() throws IOException {

        when(bookRepository.findByIsbn(bookRequest.getIsbn())).thenReturn(empty());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.requestToEntity(bookRequest)).thenReturn(book);
        when(categoryService.getCategoryById(bookRequest.getCategoryId())).thenReturn(categoryResponse);
        when(authorService.getauthorsbyids(bookRequest.getAuthorIds())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(book);


        bookService.createBook(bookRequest, file);


        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).uploadFile(file);

        verify(bookRepository, times(1)).findByIsbn(bookRequest.getIsbn());
        verify(bookMapper, times(1)).requestToEntity(bookRequest);
        verify(categoryService, times(1)).getCategoryById(bookRequest.getCategoryId());
        verify(authorService, times(1)).getauthorsbyids(bookRequest.getAuthorIds());
        verify(bookRepository, times(2)).save(any(Book.class));

    }

    @Test
    void givenCreateBook_WhenBookIsbnIsNotUnique_ThenThrowResourceAlreadyExistException() {

        when(bookRepository.findByIsbn(bookRequest.getIsbn())).thenReturn(Optional.of(book));

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> bookService.createBook(bookRequest, file));

        assertEquals("Book ISBN already exists: " + bookRequest.getIsbn(), exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));

    }

    @Test
    void givenUploadBookImage_WhenImageUploadFails_ThenThrowImageProcessingException() throws IOException {
        when(bookRepository.findByIsbn(bookRequest.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.requestToEntity(bookRequest)).thenReturn(book);
        when(categoryService.getCategoryById(bookRequest.getCategoryId())).thenReturn(categoryResponse);
        when(authorService.getauthorsbyids(bookRequest.getAuthorIds())).thenReturn(authors);
        when(bookRepository.save(book)).thenReturn(book);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        doThrow(new IOException("File upload failed")).when(storageService).uploadFile(file);

        ImageProcessingException exception = assertThrows(
                ImageProcessingException.class,
                () -> bookService.createBook(bookRequest, file)
        );

        assertEquals("Image not uploaded for book id:" + book.getId(), exception.getMessage());

        verify(bookRepository, times(1)).save(any(Book.class));

        verify(storageService, times(1)).uploadFile(file);

    }

    @Test
    void givenGetAllBooks_WhenBooksExist_ThenReturnPaginatedBookResponses() {
        int pageNumber = 0;
        int pageSize = 5;

        Book book1 = Book.builder().id(1L).name("Book One").isbn("111111").build();
        Book book2 = Book.builder().id(2L).name("Book Two").isbn("222222").build();

        List<Book> bookList = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(bookList, PageRequest.of(pageNumber, pageSize), bookList.size());

        BookResponse bookResponse1 = BookResponse.builder().id(1L).name("Book One").isbn("111111").build();
        BookResponse bookResponse2 = BookResponse.builder().id(2L).name("Book Two").isbn("222222").build();

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.entityToResponse(book1)).thenReturn(bookResponse1);
        when(bookMapper.entityToResponse(book2)).thenReturn(bookResponse2);


        Page<BookResponse> result = bookService.getAllBooks(pageNumber, pageSize);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Book One", result.getContent().get(0).getName());
        assertEquals("Book Two", result.getContent().get(1).getName());

        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, times(1)).entityToResponse(book1);
        verify(bookMapper, times(1)).entityToResponse(book2);
    }


    @Test
    void givenGetAllBooks_WhenNoBooksExist_ThenThrowResourceNotFoundException() {

        int pageNumber = 0;
        int pageSize = 5;

        Page<Book> emptyPage = new PageImpl<>(List.of(), PageRequest.of(pageNumber, pageSize), 0);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getAllBooks(pageNumber, pageSize)
        );

        assertEquals("No books available to retrieve. The library is empty.", exception.getMessage());

        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, never()).entityToResponse(any(Book.class));
    }

    @Test
    void givenGetBookById_WhenBookExists_ThenReturnBookResponse() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.entityToResponse(book)).thenReturn(bookResponse);


        BookResponse result = bookService.getBookById(1L);


        assertEquals(bookResponse.getId(), result.getId());
        assertEquals(bookResponse.getName(), result.getName());
        assertEquals(bookResponse.getIsbn(), result.getIsbn());
        assertEquals(bookResponse.getDescription(), result.getDescription());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).entityToResponse(book);
    }


    @Test
    void givenGetBookById_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() {


        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bookService.getBookById(1L));

        assertEquals("book not exist with id: 1", exception.getMessage());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, never()).entityToResponse(any(Book.class));
    }

    @Test
    void givenDeleteBookById_WhenBookExists_ThenDeleteBookSuccessfully() throws IOException {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(storageService.deleteFile(book.getBookImage())).thenReturn("Deleted successfully");
        doNothing().when(bookRepository).deleteById(1L);


        bookService.deleteBookById(1L);


        verify(bookRepository, times(2)).findById(1L);
        verify(storageService, times(1)).deleteFile("atomic_habits.jpg");
        verify(bookRepository, times(1)).deleteById(1L);
    }



    @Test
    void givenDeleteBookById_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,
                ()->bookService.deleteBookById(1L));

        assertEquals("book not exist for deleting with id: 1",exception.getMessage());

        verify(bookRepository, times(1)).findById(1L);
        verify(storageService, never()).deleteFile(anyString());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void givenDeleteBookById_WhenImageDeletionFails_ThenThrowImageProcessingException() throws IOException {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doThrow(new IOException("Failed to delete image")).when(storageService).deleteFile(book.getBookImage());

        ImageProcessingException exception = assertThrows(
                ImageProcessingException.class,
                () -> bookService.deleteBookById(bookId)
        );

        assertEquals("Not deleted Book image with id:" + bookId, exception.getMessage());

        verify(bookRepository, times(2)).findById(bookId);
        verify(storageService, times(1)).deleteFile(book.getBookImage());
        verify(bookRepository, never()).deleteById(anyLong());
    }


    @Test
    void givenUpdateBook_WhenBookExists_ThenUpdateBookSuccessfully() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(categoryService.getCategoryById(bookRequest.getCategoryId())).thenReturn(categoryResponse);
        when(authorService.getauthorsbyids(bookRequest.getAuthorIds())).thenReturn(authors);
        when(storageService.uploadFile(file)).thenReturn("updated_image.jpg");
        when(storageService.deleteFile(book.getBookImage())).thenReturn("Deleted successfully");
        when(bookRepository.save(any(Book.class))).thenReturn(book);


        bookService.updateBook(book.getId(), bookRequest, file);

        assertEquals("Atomic Habits", book.getName());
        assertEquals("A guide to building good habits.", book.getDescription());
        assertEquals(10, book.getStockCount());
        assertEquals("updated_image.jpg", book.getBookImage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(categoryService, times(1)).getCategoryById(bookRequest.getCategoryId());
        verify(authorService, times(1)).getauthorsbyids(bookRequest.getAuthorIds());
        verify(storageService, times(1)).uploadFile(file);
        verify(storageService, times(1)).deleteFile("atomic_habits.jpg");
        verify(bookRepository, times(1)).save(book);
    }


    @Test
    void givenUpdateBook_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {

        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.updateBook(bookId, bookRequest, file)
        );

        assertEquals("Book not found for update with id: " + bookId, exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(categoryService, never()).getCategoryById(anyLong());
        verify(authorService, never()).getauthorsbyids(anyList());
        verify(storageService, never()).uploadFile(any(MultipartFile.class));
        verify(storageService, never()).deleteFile(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenUpdateBook_WhenImageUploadFails_ThenThrowRuntimeException() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(categoryService.getCategoryById(bookRequest.getCategoryId())).thenReturn(categoryResponse);
        when(authorService.getauthorsbyids(bookRequest.getAuthorIds())).thenReturn(authors);
        doThrow(new IOException("Upload failed")).when(storageService).uploadFile(file);


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookService.updateBook(book.getId(), bookRequest, file)
        );

        assertEquals("Error uploading book image.", exception.getMessage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).uploadFile(file);
        verify(storageService, never()).deleteFile(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenUpdateBook_WhenNoNewImageProvided_ThenKeepExistingImage() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(categoryService.getCategoryById(bookRequest.getCategoryId())).thenReturn(categoryResponse);
        when(authorService.getauthorsbyids(bookRequest.getAuthorIds())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(book);


        bookService.updateBook(book.getId(), bookRequest, null); // No file provided


        assertEquals("atomic_habits.jpg", book.getBookImage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(categoryService, times(1)).getCategoryById(bookRequest.getCategoryId());
        verify(authorService, times(1)).getauthorsbyids(bookRequest.getAuthorIds());
        verify(storageService, never()).uploadFile(any(MultipartFile.class));
        verify(storageService, never()).deleteFile(anyString());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenUploadBookImage_WhenBookExists_ThenUploadImageSuccessfully() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(storageService.uploadFile(file)).thenReturn("uploaded_image.jpg");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.uploadBookImage(book.getId(), file);


        assertEquals("uploaded_image.jpg", book.getBookImage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).uploadFile(file);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenUploadBookImage_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {

        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.uploadBookImage(bookId, file)
        );

        assertEquals("Book Image not found with id: " + bookId, exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(storageService, never()).uploadFile(any(MultipartFile.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenUploadBookImage_WhenFileIsEmptyOrNull_ThenThrowIOException() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        MultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);


        IOException exception = assertThrows(
                IOException.class,
                () -> bookService.uploadBookImage(book.getId(), emptyFile)
        );

        assertEquals("Cannot upload empty or null file.", exception.getMessage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, never()).uploadFile(any(MultipartFile.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenDownloadBookImage_WhenBookAndImageExist_ThenReturnImageBytes() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(storageService.downloadFile(book.getBookImage())).thenReturn(new byte[]{1, 2, 3, 4, 5});


        byte[] result = bookService.downloadBookImage(book.getId());


        assertNotNull(result);
        assertEquals(5, result.length);
        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).downloadFile(book.getBookImage());
    }

    @Test
    void givenDownloadBookImage_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.downloadBookImage(bookId)
        );

        assertEquals("Book not found with id: {} for downloading " + bookId, exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(storageService, never()).downloadFile(anyString());
    }


    @Test
    void givenDownloadBookImage_WhenImageDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {

        book.setBookImage(null); // Simulating that no image exists
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.downloadBookImage(book.getId())
        );

        assertEquals("Image not found for book ID: " + book.getId(), exception.getMessage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, never()).downloadFile(anyString());
    }

    @Test
    void givenDownloadBookImage_WhenDownloadFails_ThenThrowIOException() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(storageService.downloadFile(book.getBookImage())).thenThrow(new IOException("Failed to download image"));


        IOException exception = assertThrows(
                IOException.class,
                () -> bookService.downloadBookImage(book.getId())
        );

        assertEquals("Failed to download image", exception.getMessage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).downloadFile(book.getBookImage());
    }

    @Test
    void givenDeleteBookImage_WhenBookAndImageExist_ThenDeleteImageSuccessfully() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(storageService.deleteFile(book.getBookImage())).thenReturn("Deleted successfully");
        when(bookRepository.save(any(Book.class))).thenReturn(book);


        bookService.deleteBookImage(book.getId());

        assertNull(book.getBookImage());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).deleteFile("atomic_habits.jpg");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenDeleteBookImage_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() throws IOException {

        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.deleteBookImage(bookId)
        );

        assertEquals("Deleting failed Image not found with id: " + bookId, exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(storageService, never()).deleteFile(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenDeleteBookImage_WhenImageDeletionFails_ThenThrowIOException() throws IOException {

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(storageService.deleteFile(book.getBookImage())).thenThrow(new IOException("Unexpected error deleting book image for book ID: 1"));

        IOException exception = assertThrows(
                IOException.class,
                () -> bookService.deleteBookImage(book.getId())
        );

        assertEquals("Unexpected error deleting book image for book ID: 1", exception.getMessage());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(storageService, times(1)).deleteFile(book.getBookImage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void givenCountBooks_WhenInvoked_ThenReturnCorrectBookCount() {

        when(bookRepository.count()).thenReturn(5L);


        Long count = bookService.countBooks();


        assertEquals(5L, count);
        verify(bookRepository, times(1)).count();
    }



    @Test
    void givenGetFilteredBooks_WhenFiltersProvided_ThenReturnFilteredPaginatedBooks() {

        String category = "Self-Help";
        String bookName = "Atomic";
        int page = 0, size = 5;

        Book book1 = Book.builder().id(1L).name("Atomic Habits").category(new Category()).build();
        Book book2 = Book.builder().id(2L).name("Atomic Focus").category(new Category()).build();
        Page<Book> bookPage = new PageImpl<>(List.of(book1, book2), PageRequest.of(page, size), 2);

        when(bookRepository.findBooksByCategory(eq(category), any(Pageable.class))).thenReturn(bookPage);

        BookResponse response1 = BookResponse.builder().id(1L).name("Atomic Habits").build();
        BookResponse response2 = BookResponse.builder().id(2L).name("Atomic Focus").build();
        when(bookMapper.entityToResponse(book1)).thenReturn(response1);
        when(bookMapper.entityToResponse(book2)).thenReturn(response2);


        Page<BookResponse> result = bookService.getFilteredBooks(category, bookName, page, size);

        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(r -> r.getName().contains("Atomic")));

        verify(bookRepository, times(1)).findBooksByCategory(eq(category), any(Pageable.class));
        verify(bookMapper, times(2)).entityToResponse(any(Book.class));
    }

    @Test
    void givenGetFilteredBooks_WhenNoFiltersProvided_ThenReturnAllPaginatedBooks() {

        int page = 0, size = 5;
        Book book = Book.builder().id(1L).name("Atomic Habits").build();
        Page<Book> bookPage = new PageImpl<>(List.of(book), PageRequest.of(page, size), 1);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        BookResponse response = BookResponse.builder().id(1L).name("Atomic Habits").build();
        when(bookMapper.entityToResponse(book)).thenReturn(response);


        Page<BookResponse> result = bookService.getFilteredBooks(null, null, page, size);


        assertEquals(1, result.getTotalElements());
        assertEquals("Atomic Habits", result.getContent().get(0).getName());

        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, times(1)).entityToResponse(any(Book.class));
    }



}