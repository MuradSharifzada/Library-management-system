package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.service.OrderService;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;
    private final StudentService studentService;

    @GetMapping
    public String showAllOrders(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {

        List<OrderResponse> orders = orderService.getAllOrders(page, size);
        int totalPages = (int) Math.ceil((double) orderService.countOrders() / size);

        model.addAttribute("orders", orders);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "order/orders";
    }


    @GetMapping("/borrow")
    public String showBorrowPage(Model model) {
        int studentCount = Math.toIntExact(studentService.countStudents());
        int bookCount = Math.toIntExact(bookService.countBooks());

        List<StudentResponse> students = studentService.getAllStudents(0, studentCount).getContent();
        List<BookResponse> books = bookService.getAllBooks(0, bookCount).getContent();

        model.addAttribute("order", new OrderRequest());
        model.addAttribute("students", students);
        model.addAttribute("books", books);

        return "order/borrow-order";
    }


    @PostMapping("/borrow")
    public String borrowOrder(@ModelAttribute OrderRequest orderRequest) {
        orderService.borrowOrder(orderRequest);
        return "redirect:/orders";
    }

    @GetMapping("/return")
    public String showReturnPage(Model model) {

        List<OrderResponse> borrowedOrders = orderService.getAllBorrowedOrders(0, Integer.MAX_VALUE);

        model.addAttribute("borrowedOrders", borrowedOrders);
        model.addAttribute("hasBorrowedOrders", !borrowedOrders.isEmpty());

        return "order/return-order";
    }


    @PostMapping("/return")
    public String returnOrder(@RequestParam Long orderId,
                              @RequestParam Long studentId,
                              @RequestParam Long bookId) {

        OrderRequest returnRequest = new OrderRequest();
        returnRequest.setStudentId(studentId);
        returnRequest.setBookId(bookId);

        orderService.returnOrder(returnRequest);
        return "redirect:/orders";
    }
}
