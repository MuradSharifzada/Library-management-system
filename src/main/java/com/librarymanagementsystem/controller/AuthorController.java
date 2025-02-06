package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.service.AuthorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;


    @GetMapping
    public String showAllAuthors(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "6") int size,
                                 Model model) {

        Page<AuthorResponse> authorPage = authorService.getAllAuthors(page, size);

        model.addAttribute("authors", authorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", authorPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "author/authors";
    }

    @GetMapping("/add")
    public String createAuthorForm(Model model) {
        model.addAttribute("author", new AuthorRequest());
        return "author/create-author";
    }

    @PostMapping("/add")
    public String createAuthor(@Valid @ModelAttribute AuthorRequest authorRequest) {
        authorService.createAuthor(authorRequest);
        return "redirect:/authors";
    }


    @GetMapping("/books/{id}")
    public String getBooksByAuthorId(@PathVariable Long id, Model model) {
        List<BookResponse> books = authorService.getBooksByAuthorId(id);
        model.addAttribute("books", books);
        model.addAttribute("authorId", id);
        return "author/author-books";
    }


    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return "redirect:/authors";
    }


    @GetMapping("/update/{id}")
    public String updateAuthorForm(@PathVariable Long id, Model model) {
        AuthorResponse authorResponse = authorService.getAuthorById(id);
        model.addAttribute("author", authorResponse);
        return "author/update-author";
    }

    @PostMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, @Valid @ModelAttribute AuthorRequest authorRequest) {
        authorService.updateAuthor(id, authorRequest);
        return "redirect:/authors";
    }

    @GetMapping("/details/{id}")
    public String getAuthorById(@PathVariable Long id, Model model) {
        AuthorResponse author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author/author-details";
    }



}
