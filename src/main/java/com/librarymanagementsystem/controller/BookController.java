package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.service.AuthorService;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;

    @GetMapping
    public String showAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            Model model) {

        Page<BookResponse> bookPage = bookService.getFilteredBooks(category, name, page, size);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("totalPages", bookPage.getTotalPages() == 0 ? 1 : bookPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", categoryService.getAllCategory(0, Integer.MAX_VALUE));
        model.addAttribute("searchQuery", name);

        return "book/books";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookRequest());
        model.addAttribute("categories", categoryService.getAllCategory(0, Integer.MAX_VALUE));
        model.addAttribute("authors", authorService.getAllAuthors(0, Integer.MAX_VALUE));
        return "book/add-book";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String createBook(@Validated(BookRequest.Create.class) @ModelAttribute("book") BookRequest bookRequest,
                             @RequestParam("imageFile") MultipartFile file) {
        bookService.createBook(bookRequest, file);
        return "redirect:/books";
    }


    @GetMapping("/update/{id}")
    public String updateBookForm(@PathVariable Long id, Model model) {
        BookResponse bookResponse = bookService.getBookById(id);

        model.addAttribute("book", bookResponse);

        model.addAttribute("categories", categoryService.getAllCategory(0, Integer.MAX_VALUE));

        model.addAttribute("authors", authorService.getAllAuthors(0, Integer.MAX_VALUE));

        return "book/update-book";
    }


    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @Validated(BookRequest.Update.class) @ModelAttribute BookRequest bookRequest,
                             @RequestParam(value = "file", required = false) MultipartFile file) {
        bookService.updateBook(id, bookRequest,file);
        return "redirect:/books";
    }


    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        BookResponse bookResponse = bookService.getBookById(id);
        model.addAttribute("book", bookResponse);
        model.addAttribute("authors",authorService.getauthorsbyids(bookResponse.getAuthorIds()));
        return "book/book-details";
    }


    @PostMapping("/{id}/image")
    public String uploadBookImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        bookService.uploadBookImage(id, file);
        return "redirect:/books/" + id;
    }


    @PostMapping("/{id}/image/delete")
    public String deleteBookImage(@PathVariable Long id) throws IOException {
        bookService.deleteBookImage(id);
        return "redirect:/books/" + id;
    }


    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getBookImage(@PathVariable Long id) throws IOException {
        return bookService.downloadBookImage(id);
    }

}
