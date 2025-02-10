package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.LibrarianRequest;
import com.librarymanagementsystem.dto.response.LibrarianResponse;
import com.librarymanagementsystem.service.LibrarianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarians")
@RequiredArgsConstructor
public class LibrarianController {

    private final LibrarianService librarianService;

    @GetMapping
    public String showAllLibrarians(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "6") int size,
                                    Model model) {
        Page<LibrarianResponse> librarianPage = librarianService.getAllLibrarians(page, size);
        model.addAttribute("librarians", librarianPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", librarianPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "librarian/librarians";
    }

    @GetMapping("/add")
    public String createLibrarianForm(Model model) {
        model.addAttribute("librarian", new LibrarianRequest());
        return "librarian/create-librarian";
    }

    @PostMapping("/add")
    public String createLibrarian(@Valid @ModelAttribute LibrarianRequest librarianRequest) {
        librarianService.createLibrarian(librarianRequest);
        return "redirect:/librarians";
    }

    @PostMapping("/delete/{id}")
    public String deleteLibrarian(@PathVariable Long id) {
        librarianService.deleteLibrarian(id);
        return "redirect:/librarians";
    }

    @GetMapping("/update/{id}")
    public String updateLibrarianForm(@PathVariable Long id, Model model) {
        LibrarianResponse librarianResponse = librarianService.getLibrarianById(id);
        model.addAttribute("librarian", librarianResponse);
        return "librarian/update-librarian";
    }

    @PostMapping("/update/{id}")
    public String updateLibrarian(@Valid @PathVariable Long id, @ModelAttribute LibrarianRequest librarianRequest) {
        librarianService.updateLibrarian(id, librarianRequest);
        return "redirect:/librarians";
    }

    @GetMapping("/details/{id}")
    public String getLibrarianById(@PathVariable Long id, Model model) {
        LibrarianResponse librarian = librarianService.getLibrarianById(id);
        model.addAttribute("librarian", librarian);
        return "librarian/librarian-details";
    }
}
