package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new category", description = "Adds a new category to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<String> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the category to create",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryRequest.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"name\": \"Self-Help\",\n" +
                                            "  \"description\": \"Books focused on personal development and growth.\",\n" +
                                            "  \"type\": \"Non-Fiction\"\n" +
                                            "}")))
            @RequestBody @Valid CategoryRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Category created successfully");
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories with pagnation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @Parameter(description = "Page number for pagination, defaults to 0", example = "0")
            @RequestParam(name = "Page Number", defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 10", example = "10")
            @RequestParam(name = "Page Size", defaultValue = "10") int pageSize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getAllCategory(pageNumber, pageSize));
    }

    @Operation(summary = "Get a category by ID", description = "Retrieves the details  category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "ID of the category to retrieve") @PathVariable(name = "id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Delete a category by ID", description = "Deletes category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @Parameter(description = "ID of the category to delete") @PathVariable(name = "id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Successfully deleted category with ID: " + id);
    }
}
