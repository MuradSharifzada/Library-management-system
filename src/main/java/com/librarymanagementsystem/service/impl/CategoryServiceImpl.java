package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.CategoryMapper;
import com.librarymanagementsystem.model.entity.Category;
import com.librarymanagementsystem.repository.CategoryRepository;
import com.librarymanagementsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public Page<CategoryResponse> getAllCategory(int pageNumber, int pageSize) {
        log.info("Attempting to retrieve all categories");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.isEmpty()) {
            log.warn("No categories found in the database");
            throw new ResourceNotFoundException("No categories found");
        } else {
            log.info("Successfully retrieved  categories");
        }

        return categories.map(categoryMapper::entityToResponse);
    }

    @Override
    public void createCategory(CategoryRequest request) {
        log.info("Attempting to create a new category with name: {}", request.getName());

        if (categoryRepository.existsByName(request.getName())) {
            log.error("Category creation failed - name {} already exists", request.getName());
            throw new ResourceAlreadyExistsException("Category name " + request.getName() + " already exists");
        }

        Category category = categoryMapper.requestToEntity(request);
        category = categoryRepository.save(category);

        log.info("Successfully created new category with ID: {} and name: {}", category.getId(), category.getName());
    }

    @Override
    public void updateCategory(Long id, CategoryRequest request) {
        log.info("Attempting to update category with ID: {}", id);

        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category update failed - category with ID: {} not found", id);
            return new ResourceNotFoundException("Category not found with ID: " + id);
        });
        Category updatedCategory = categoryMapper.requestToEntity(request);
        updatedCategory.setId(existingCategory.getId());

        categoryRepository.save(updatedCategory);

        log.info("Successfully updated category with ID: {}", id);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("Attempting to retrieve category with ID: {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category retrieval failed - category with ID: {} not found", id);
            return new ResourceNotFoundException("Category not found with ID: " + id);
        });

        log.info("Successfully retrieved category with ID: {}", id);

        return categoryMapper.entityToResponse(category);
    }

    @Override
    public void deleteCategoryById(Long id) {

        log.info("Attempt to delete category with ID: {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category deleting failed - category with ID: {} not found", id);
            return new ResourceNotFoundException("Category not found with ID: " + id + " for deleting");
        });
        log.info("Successfully deleted category with ID: {}", category.getId());
        categoryRepository.deleteById(category.getId());

    }
    @Override
    public Long countCategories() {
        return categoryRepository.count();
    }
}
