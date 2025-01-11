package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors/create")
    public String createAuthorForm(Model model) {
        model.addAttribute("authorRequest", new AuthorRequest());
        return "author/create-author";
    }

    @PostMapping("/authors/create")
    public String createAuthor(
            @Valid @ModelAttribute("authorRequest") AuthorRequest authorRequest,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "author/create-author";
        }
        authorService.createAuthor(authorRequest);
        return "redirect:/authors";
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "author/list";
    }


    @GetMapping("/authors/{id:\\d+}")
    public String getAuthorById(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("author", authorService.getAuthorById(id));
            return "author/author";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Author not found with ID: " + id);
            return "error";
        }
    }

    @GetMapping("/authors/delete/{id:\\d+}")
    public String deleteAuthorById(@PathVariable Long id) {
        try {
            authorService.deleteAuthorById(id);
        } catch (Exception e) {
            return "redirect:/error?message=Unable to delete author with ID: " + id;
        }
        return "redirect:/authors";
    }

    @PostMapping("/authors/update")
    public String updateAuthor(
            @RequestParam Long id,
            @Valid @ModelAttribute("authorRequest") AuthorRequest authorRequest,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("authorRequest", authorRequest);
            model.addAttribute("authorId", id); // Include the ID for re-rendering
            return "author/edit-author";
        }
        try {
            authorService.updateAuthor(id, authorRequest);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update author with ID: " + id);
            return "error";
        }
        return "redirect:/authors";
    }
}

