package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.exception.CustomIllegalStateException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.OrderMapper;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.model.entity.Order;
import com.librarymanagementsystem.model.entity.Student;
import com.librarymanagementsystem.model.enums.Status;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.OrderRepository;
import com.librarymanagementsystem.repository.StudentRepository;
import com.librarymanagementsystem.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrders(int pageNumber, int pageSize) {
        log.info("Fetching orders: page {}, size {}", pageNumber, pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> orders = orderRepository.findAll(pageable);

        if (!orders.hasContent()) {
            log.warn("No orders found for page {}, size {}", pageNumber, pageSize);
            throw new ResourceNotFoundException("No Orders available to retrieve.");
        }

        log.info("Orders fetched successfully: page {}, size {}", pageNumber, pageSize);
        return orders.getContent()
                .stream()
                .map(orderMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void borrowOrder(OrderRequest request) {
        log.info("Processing borrow request: book ID {}, student ID {}", request.getBookId(), request.getStudentId());

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + request.getBookId() + " not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + request.getStudentId() + " not found"));

        if (book.getStockCount() <= 0) {
            log.error("Borrow denied: Book ID {} is out of stock", book.getId());
            throw new CustomIllegalStateException("This book is currently out of stock.");
        }

        boolean hasActiveBorrow = orderRepository.existsByStudentAndReturnDateIsNull(student);

        if (hasActiveBorrow) {
            log.error("Borrow denied: Student ID {} already has an active borrowed book", student.getId());
            throw new CustomIllegalStateException("You have already borrowed a book. Please return it before borrowing another.");
        }

        Order order = new Order();
        order.setBook(book);
        order.setStudent(student);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.BORROWED);
        order.setReturnDate(null);

        book.setStockCount(book.getStockCount() - 1);

        bookRepository.save(book);
        orderRepository.save(order);

        log.info("Borrow successful: Student ID {}, Book ID {}", student.getId(), book.getId());
    }

    @Override
    @Transactional
    public void returnOrder(OrderRequest orderRequest) {
        log.info("Processing return request: book ID {}, student ID {}", orderRequest.getBookId(), orderRequest.getStudentId());

        Book book = bookRepository.findById(orderRequest.getBookId())
                .orElseThrow(() -> {
                    log.error("Book not found for return: ID {}", orderRequest.getBookId());
                    return new ResourceNotFoundException("Book with ID " + orderRequest.getBookId() + " not found");
                });

        Student student = studentRepository.findById(orderRequest.getStudentId())
                .orElseThrow(() -> {
                    log.error("Student not found for return: ID {}", orderRequest.getStudentId());
                    return new ResourceNotFoundException("Student with ID " + orderRequest.getStudentId() + " not found");
                });

        Order order = orderRepository.findByStudentIdAndBookIdAndReturnDateIsNull(
                orderRequest.getStudentId(),
                orderRequest.getBookId()
        ).orElseThrow(() -> {
            log.error("No active order found for return: Student ID {}, Book ID {}", orderRequest.getStudentId(), orderRequest.getBookId());
            return new ResourceNotFoundException("No active borrowing record found for this student and book.");
        });

        order.setReturnDate(LocalDateTime.now());
        order.setStatus(Status.RETURNED);
        orderRepository.save(order);
        log.info("Order returned: Student ID {}, Book ID {}", student.getId(), book.getId());

        book.setStockCount(book.getStockCount() + 1);
        bookRepository.save(book);
        log.info("Stock updated: Book ID {}, current stock {}", book.getId(), book.getStockCount());
    }

    @Override
    public Long countOrders() {
        return orderRepository.count();
    }

    @Override
    public List<OrderResponse> getAllBorrowedOrders(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        return ordersPage.getContent().stream()
                .filter(order -> order.getReturnDate() == null)
                .map(orderMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
