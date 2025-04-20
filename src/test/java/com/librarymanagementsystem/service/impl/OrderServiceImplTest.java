package com.librarymanagementsystem.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.exception.CustomIllegalStateException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.repository.OrderRepository;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.StudentRepository;
import com.librarymanagementsystem.mapper.OrderMapper;
import com.librarymanagementsystem.service.impl.OrderServiceImpl;
import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.model.entity.Order;
import com.librarymanagementsystem.model.entity.Student;
import com.librarymanagementsystem.model.enums.Status;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private Book book;
    private Student student;


    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .name("Atomic Habits")
                .isbn("9780735211292")
                .stockCount(5)
                .build();

        student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        StudentResponse studentResponse = StudentResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        orderRequest = OrderRequest.builder()
                .bookId(1L)
                .studentId(1L)
                .build();

        order = Order.builder()
                .id(1L)
                .book(book)
                .student(student)
                .orderDate(LocalDateTime.now())
                .status(Status.BORROWED)
                .build();

        BookResponse bookResponse = BookResponse.builder()
                .id(1L)
                .name("Atomic Habits")
                .isbn("9780735211292")
                .build();

        orderResponse = OrderResponse.builder()
                .id(1L)
                .book(bookResponse)
                .student(studentResponse)
                .status(Status.BORROWED)
                .build();
    }


    @Test
    void givenBorrowOrder_WhenBookAndStudentExist_ThenCreateOrderSuccessfully() {

        Long bookId = orderRequest.getBookId();
        Long studentId = orderRequest.getStudentId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(orderRepository.existsByStudentAndReturnDateIsNull(student)).thenReturn(false);
        Order order = new Order();
        order.setBook(book);
        order.setStudent(student);
        order.setStatus(Status.BORROWED);

        when(orderMapper.toOrder(any(), eq(book), eq(student))).thenReturn(order);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        orderService.borrowOrder(orderRequest);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(orderRepository, times(1)).existsByStudentAndReturnDateIsNull(student);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(1)).save(book);
        assertEquals(4, book.getStockCount());
    }



    @Test
    void givenBorrowOrder_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() {

        when(bookRepository.findById(orderRequest.getBookId())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.borrowOrder(orderRequest)
        );

        assertEquals("Book with ID " + orderRequest.getBookId() + " not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(orderRequest.getBookId());
        verify(studentRepository, never()).findById(anyLong());
    }

    @Test
    void givenBorrowOrder_WhenStudentDoesNotExist_ThenThrowResourceNotFoundException() {

        when(bookRepository.findById(orderRequest.getBookId())).thenReturn(Optional.of(book));
        when(studentRepository.findById(orderRequest.getStudentId())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.borrowOrder(orderRequest)
        );

        assertEquals("Student with ID " + orderRequest.getStudentId() + " not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(orderRequest.getBookId());
        verify(studentRepository, times(1)).findById(orderRequest.getStudentId());
        verify(orderRepository, never()).existsByStudentAndReturnDateIsNull(any());
    }


    @Test
    void givenBorrowOrder_WhenBookOutOfStock_ThenThrowCustomIllegalStateException() {
        book.setStockCount(0);
        when(bookRepository.findById(orderRequest.getBookId())).thenReturn(Optional.of(book));
        when(studentRepository.findById(orderRequest.getStudentId())).thenReturn(Optional.of(student));

        CustomIllegalStateException exception = assertThrows(
                CustomIllegalStateException.class,
                () -> orderService.borrowOrder(orderRequest)
        );

        assertEquals("This book is currently out of stock.", exception.getMessage());
        verify(bookRepository, times(1)).findById(orderRequest.getBookId());
        verify(studentRepository, times(1)).findById(orderRequest.getStudentId());
        verify(orderRepository, never()).existsByStudentAndReturnDateIsNull(any());
        verify(orderRepository, never()).save(any(Order.class));
    }


    @Test
    void givenBorrowOrder_WhenStudentHasActiveBorrow_ThenThrowCustomIllegalStateException() {
        when(bookRepository.findById(orderRequest.getBookId())).thenReturn(Optional.of(book));
        when(studentRepository.findById(orderRequest.getStudentId())).thenReturn(Optional.of(student));
        when(orderRepository.existsByStudentAndReturnDateIsNull(student)).thenReturn(true);

        CustomIllegalStateException exception = assertThrows(
                CustomIllegalStateException.class,
                () -> orderService.borrowOrder(orderRequest)
        );

        assertEquals("You have already borrowed a book. Please return it before borrowing another.", exception.getMessage());
        verify(bookRepository, times(1)).findById(orderRequest.getBookId());
        verify(studentRepository, times(1)).findById(orderRequest.getStudentId());
        verify(orderRepository, times(1)).existsByStudentAndReturnDateIsNull(student);
        verify(orderRepository, never()).save(any(Order.class));
    }


    @Test
    void givenReturnOrder_WhenOrderExists_ThenReturnOrderSuccessfully() {
        // Arrange
        Long studentId = orderRequest.getStudentId();
        Long bookId = orderRequest.getBookId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(orderRepository.findByStudentIdAndBookIdAndReturnDateIsNull(studentId, bookId))
                .thenReturn(Optional.of(order));

        // Act
        orderService.returnOrder(studentId, bookId);

        // Assert
        assertNotNull(order.getReturnDate());
        assertEquals(Status.RETURNED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(bookRepository, times(1)).save(book);
        assertEquals(6, book.getStockCount());
    }


    @Test
    void givenReturnOrder_WhenBookDoesNotExist_ThenThrowResourceNotFoundException() {
        // Arrange
        Long studentId = orderRequest.getStudentId();
        Long bookId = orderRequest.getBookId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.returnOrder(studentId, bookId)
        );

        assertEquals("Book with ID " + bookId + " not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(studentRepository, never()).findById(anyLong());
        verify(orderRepository, never()).findByStudentIdAndBookIdAndReturnDateIsNull(anyLong(), anyLong());
    }



    @Test
    void givenReturnOrder_WhenStudentDoesNotExist_ThenThrowResourceNotFoundException() {
        // Arrange
        Long studentId = orderRequest.getStudentId();
        Long bookId = orderRequest.getBookId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.returnOrder(studentId, bookId)
        );

        assertEquals("Student with ID " + studentId + " not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(orderRepository, never()).findByStudentIdAndBookIdAndReturnDateIsNull(anyLong(), anyLong());
    }


    @Test
    void givenReturnOrder_WhenNoActiveOrderExists_ThenThrowResourceNotFoundException() {
        // Arrange
        Long studentId = orderRequest.getStudentId();
        Long bookId = orderRequest.getBookId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(orderRepository.findByStudentIdAndBookIdAndReturnDateIsNull(studentId, bookId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.returnOrder(studentId, bookId)
        );

        assertEquals("No active borrowing record found for this student and book.", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(orderRepository, times(1)).findByStudentIdAndBookIdAndReturnDateIsNull(studentId, bookId);
    }


    @Test
    void givenCountOrders_WhenInvoked_ThenReturnCorrectOrderCount() {

        when(orderRepository.count()).thenReturn(10L);


        Long count = orderService.countOrders();

        assertEquals(10L, count);
        verify(orderRepository, times(1)).count();
    }

    @Test
    void givenGetAllBorrowedOrders_WhenBorrowedOrdersExist_ThenReturnPaginatedBorrowedOrderResponses() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Order> borrowedOrdersList = List.of(order);
        Page<Order> ordersPage = new PageImpl<>(borrowedOrdersList, pageable, borrowedOrdersList.size());

        when(orderRepository.findAll(pageable)).thenReturn(ordersPage);
        when(orderRepository.findAll(pageable)).thenReturn(ordersPage);
        when(orderMapper.entityToResponse(order)).thenReturn(orderResponse);


        List<OrderResponse> result = orderService.getAllBorrowedOrders(pageNumber, pageSize);


        assertEquals(1, result.size());
        assertEquals(orderResponse.getId(), result.get(0).getId());
        assertEquals(orderResponse.getStatus(), result.get(0).getStatus());
        assertEquals(orderResponse.getBook().getName(), result.get(0).getBook().getName());
        assertEquals(orderResponse.getStudent(), result.get(0).getStudent());

        verify(orderRepository, times(1)).findAll(pageable);
        verify(orderMapper, times(1)).entityToResponse(order);
    }

    @Test
    void givenGetAllBorrowedOrders_WhenNoBorrowedOrdersExist_ThenReturnEmptyList() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Order> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(orderRepository.findAll(pageable)).thenReturn(emptyPage);


        List<OrderResponse> result = orderService.getAllBorrowedOrders(pageNumber, pageSize);


        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll(pageable);
        verify(orderMapper, never()).entityToResponse(any(Order.class));
    }



}







