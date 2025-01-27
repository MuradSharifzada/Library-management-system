package com.librarymanagementsystem.controller;


import com.librarymanagementsystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final StudentService studentService;
    private final OrderService orderService;
    private final CategoryService categoryService;



    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("totalBooks", bookService.countBooks());
        model.addAttribute("totalStudents", studentService.countStudents());
        model.addAttribute("totalOrders", orderService.countOrders());
        model.addAttribute("totalAuthors", authorService.countAuthors());
        model.addAttribute("totalCategories",categoryService.countCategories());

        return "admin-dashboard";
    }
}