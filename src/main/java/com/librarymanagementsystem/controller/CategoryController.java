package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping()
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Category created successfully");
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoryById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Successfully deleted category with ID: " + id);

    }
}
