package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public String showAllCategories(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model) {
        Page<CategoryResponse> categoryPage = categoryService.getAllCategory(page, size);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "category/categories";
    }


    @GetMapping("/add")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new CategoryRequest());
        return "category/create-category";
    }


    @PostMapping("/add")
    public String createCategory(@Validated(CategoryRequest.Create.class) @ModelAttribute CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
        return "redirect:/categories";
    }


    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        model.addAttribute("category", categoryResponse);
        return "category/category-details";
    }


    @GetMapping("/update/{id}")
    public String updateCategoryForm(@PathVariable Long id, Model model) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        model.addAttribute("category", categoryResponse);
        return "category/update-category";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @Validated(CategoryRequest.Update.class) @ModelAttribute CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }
}
