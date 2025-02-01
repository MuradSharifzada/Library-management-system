package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Page<CategoryResponse> getAllCategory(int pageNumber, int pageSize);

    void createCategory(CategoryRequest request);

    void updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    void deleteCategoryById(Long id);

    Long countCategories();
}
