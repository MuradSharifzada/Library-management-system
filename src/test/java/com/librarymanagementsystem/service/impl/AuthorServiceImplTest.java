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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorRequest authorRequest;
    private AuthorResponse authorResponse;

    @BeforeEach
    void setUp() {
        authorRequest = AuthorRequest.builder()
                .firstName("James")
                .lastName("Clear")
                .birthDay(LocalDate.of(1986, 1, 22))
                .biography("James Clear is the author of Atomic Habits, a bestseller on building good habits.")
                .build();

        author = Author.builder()
                .id(1L)
                .firstName("James")
                .lastName("Clear")
                .birthDay(LocalDate.of(1986, 1, 22))
                .biography("James Clear is the author of Atomic Habits, a bestseller on building good habits.")
                .books(new ArrayList<>())
                .build();

        authorResponse = AuthorResponse.builder()
                .id(1L)
                .firstName("James")
                .lastName("Clear")
                .birthDay(LocalDate.of(1986, 1, 22))
                .biography("James Clear is the author of Atomic Habits, a bestseller on building good habits.")
                .build();
    }

    @Test
    void givenCreateAuthor_WhenAuthorDoesNotExist_ThenCreateAuthorSuccessfully() {

        when(authorRepository.existsByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName()))
                .thenReturn(false);
        when(authorMapper.requestToEntity(authorRequest)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);


        authorService.createAuthor(authorRequest);


        verify(authorRepository, times(1)).existsByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName());
        verify(authorMapper, times(1)).requestToEntity(authorRequest);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void givenCreateAuthor_WhenAuthorAlreadyExists_ThenThrowResourceAlreadyExistsException() {

        when(authorRepository.existsByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName()))
                .thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> authorService.createAuthor(authorRequest)
        );

        assertEquals("Author James Clear already exists", exception.getMessage());

        verify(authorRepository, times(1)).existsByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName());
        verify(authorMapper, never()).requestToEntity(any(AuthorRequest.class));
        verify(authorRepository, never()).save(any(Author.class));
    }


    @Test
    void givenGetAllAuthors_WhenAuthorsExist_ThenReturnPaginatedAuthorResponses() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        List<Author> authorsList = List.of(author);
        Page<Author> authorPage = new PageImpl<>(authorsList, pageable, authorsList.size());

        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        when(authorMapper.entityToResponse(author)).thenReturn(authorResponse);


        Page<AuthorResponse> result = authorService.getAllAuthors(pageNumber, pageSize);


        assertEquals(1, result.getTotalElements());
        assertEquals(authorResponse.getFirstName(), result.getContent().get(0).getFirstName());
        assertEquals(authorResponse.getLastName(), result.getContent().get(0).getLastName());

        verify(authorRepository, times(1)).findAll(pageable);
        verify(authorMapper, times(1)).entityToResponse(author);
    }

    @Test
    void givenGetAllAuthors_WhenNoAuthorsExist_ThenThrowResourceNotFoundException() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Author> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(authorRepository.findAll(pageable)).thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getAllAuthors(pageNumber, pageSize)
        );

        assertEquals("No authors found", exception.getMessage());
        verify(authorRepository, times(1)).findAll(pageable);
        verify(authorMapper, never()).entityToResponse(any(Author.class));
    }

    @Test
    void givenGetAuthorById_WhenAuthorExists_ThenReturnAuthorResponse() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.entityToResponse(author)).thenReturn(authorResponse);


        AuthorResponse result = authorService.getAuthorById(authorId);

        assertEquals(authorResponse.getId(), result.getId());
        assertEquals(authorResponse.getFirstName(), result.getFirstName());
        assertEquals(authorResponse.getLastName(), result.getLastName());
        assertEquals(authorResponse.getBiography(), result.getBiography());

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).entityToResponse(author);
    }

    @Test
    void givenGetAuthorById_WhenAuthorDoesNotExist_ThenThrowResourceNotFoundException() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getAuthorById(authorId)
        );

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, never()).entityToResponse(any(Author.class));
    }
    @Test
    void givenUpdateAuthor_WhenAuthorExists_ThenUpdateAuthorSuccessfully() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.requestToEntity(authorRequest)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);


        authorService.updateAuthor(authorId, authorRequest);

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).requestToEntity(authorRequest);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void givenUpdateAuthor_WhenAuthorDoesNotExist_ThenThrowResourceNotFoundException() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.updateAuthor(authorId, authorRequest)
        );

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, never()).requestToEntity(any(AuthorRequest.class));
        verify(authorRepository, never()).save(any(Author.class));
    }


    @Test
    void givenDeleteAuthorById_WhenAuthorExists_ThenDeleteAuthorSuccessfully() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(authorId);

        authorService.deleteAuthorById(authorId);

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void givenDeleteAuthorById_WhenAuthorDoesNotExist_ThenThrowResourceNotFoundException() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.deleteAuthorById(authorId)
        );

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void givenGetBooksByAuthorId_WhenBooksExist_ThenReturnBookResponses() {

        Long authorId = 1L;
        Book book1 = Book.builder().id(101L).name("Atomic Habits").build();
        Book book2 = Book.builder().id(102L).name("Deep Work").build();
        author.setBooks(List.of(book1, book2));

        BookResponse bookResponse1 = BookResponse.builder().id(101L).name("Atomic Habits").build();
        BookResponse bookResponse2 = BookResponse.builder().id(102L).name("Deep Work").build();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookMapper.entityToResponse(book1)).thenReturn(bookResponse1);
        when(bookMapper.entityToResponse(book2)).thenReturn(bookResponse2);


        List<BookResponse> result = authorService.getBooksByAuthorId(authorId);


        assertEquals(2, result.size());
        assertEquals("Atomic Habits", result.get(0).getName());
        assertEquals("Deep Work", result.get(1).getName());

        verify(authorRepository, times(1)).findById(authorId);
        verify(bookMapper, times(2)).entityToResponse(any(Book.class));
    }

    @Test
    void givenGetBooksByAuthorId_WhenNoBooksExist_ThenThrowResourceNotFoundException() {

        Long authorId = 1L;
        author.setBooks(new ArrayList<>());

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getBooksByAuthorId(authorId)
        );

        assertEquals("No books found for author ID: " + authorId, exception.getMessage());

        verify(authorRepository, times(1)).findById(authorId);
        verify(bookMapper, never()).entityToResponse(any(Book.class));
    }


    @Test
    void givenGetBooksByAuthorId_WhenAuthorDoesNotExist_ThenThrowResourceNotFoundException() {

        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getBooksByAuthorId(authorId)
        );

        assertEquals("Author not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(authorId);
        verify(bookMapper, never()).entityToResponse(any(Book.class));
    }


    @Test
    void givenGetAuthorsByIds_WhenAuthorsExist_ThenReturnAuthorList() {

        List<Long> authorIds = List.of(1L, 2L);
        Author author2 = Author.builder()
                .id(2L)
                .firstName("Cal")
                .lastName("Newport")
                .biography("Author of Deep Work")
                .books(new ArrayList<>())
                .build();

        List<Author> authorsList = List.of(author, author2);

        when(authorRepository.findAllById(authorIds)).thenReturn(authorsList);


        List<Author> result = authorService.getauthorsbyids(authorIds);


        assertEquals(2, result.size());
        assertEquals("James", result.get(0).getFirstName());
        assertEquals("Cal", result.get(1).getFirstName());

        verify(authorRepository, times(1)).findAllById(authorIds);
    }

    @Test
    void givenGetAuthorsByIds_WhenNoAuthorsExist_ThenThrowResourceNotFoundException() {

        List<Long> authorIds = List.of(1L, 2L);
        when(authorRepository.findAllById(authorIds)).thenReturn(List.of());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getauthorsbyids(authorIds)
        );

        assertEquals("No valid authors found for the given IDs.", exception.getMessage());

        verify(authorRepository, times(1)).findAllById(authorIds);
    }

    @Test
    void givenCountAuthors_WhenInvoked_ThenReturnCorrectAuthorCount() {

        when(authorRepository.count()).thenReturn(10L);


        Long result = authorService.countAuthors();

        assertEquals(10L, result);
        verify(authorRepository, times(1)).count();
    }


}