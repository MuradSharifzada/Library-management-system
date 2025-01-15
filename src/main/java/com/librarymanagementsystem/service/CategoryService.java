package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategory(int pageNumber,int pageSize);

    void createCategory(CategoryRequest request);

    void updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    void deleteCategoryById(Long id);
}
