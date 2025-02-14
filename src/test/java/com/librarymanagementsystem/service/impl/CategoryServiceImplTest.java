package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.CategoryMapper;
import com.librarymanagementsystem.model.entity.Category;
import com.librarymanagementsystem.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        categoryRequest = CategoryRequest.builder()
                .name("Self-Help")
                .description("Books focused on personal development and growth.")
                .type("Non-Fiction")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Self-Help")
                .description("Books focused on personal development and growth.")
                .type("Non-Fiction")
                .books(new ArrayList<>())
                .build();

        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Self-Help")
                .description("Books focused on personal development and growth.")
                .type("Non-Fiction")
                .build();
    }


    @Test
    void givenGetAllCategory_WhenCategoriesExist_ThenReturnPaginatedCategoryResponses() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        List<Category> categoryList = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.entityToResponse(category)).thenReturn(categoryResponse);


        Page<CategoryResponse> result = categoryService.getAllCategory(pageNumber, pageSize);

        assertEquals(1, result.getTotalElements());
        assertEquals("Self-Help", result.getContent().get(0).getName());
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).entityToResponse(category);
    }

    @Test
    void givenGetAllCategory_WhenNoCategoriesExist_ThenThrowResourceNotFoundException() {

        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Category> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(categoryRepository.findAll(pageable)).thenReturn(emptyPage);


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getAllCategory(pageNumber, pageSize)
        );

        assertEquals("No categories found", exception.getMessage());
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, never()).entityToResponse(any(Category.class));
    }

    @Test
    void givenCreateCategory_WhenCategoryDoesNotExist_ThenCreateCategorySuccessfully() {

        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(false);
        when(categoryMapper.requestToEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);


        categoryService.createCategory(categoryRequest);


        verify(categoryRepository, times(1)).existsByName(categoryRequest.getName());
        verify(categoryMapper, times(1)).requestToEntity(categoryRequest);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void givenCreateCategory_WhenCategoryAlreadyExists_ThenThrowResourceAlreadyExistsException() {

        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(true);


        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> categoryService.createCategory(categoryRequest)
        );

        assertEquals("Category name Self-Help already exists", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName(categoryRequest.getName());
        verify(categoryMapper, never()).requestToEntity(any(CategoryRequest.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }


    @Test
    void givenUpdateCategory_WhenCategoryExists_ThenUpdateCategorySuccessfully() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.requestToEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);


        categoryService.updateCategory(categoryId, categoryRequest);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).requestToEntity(categoryRequest);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void givenUpdateCategory_WhenCategoryDoesNotExist_ThenThrowResourceNotFoundException() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, categoryRequest)
        );

        assertEquals("Category not found with ID: " + categoryId, exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, never()).requestToEntity(any(CategoryRequest.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }


    @Test
    void givenGetCategoryById_WhenCategoryExists_ThenReturnCategoryResponse() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.entityToResponse(category)).thenReturn(categoryResponse);


        CategoryResponse result = categoryService.getCategoryById(categoryId);


        assertEquals(categoryResponse.getId(), result.getId());
        assertEquals(categoryResponse.getName(), result.getName());
        assertEquals(categoryResponse.getDescription(), result.getDescription());
        assertEquals(categoryResponse.getType(), result.getType());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).entityToResponse(category);
    }

    @Test
    void givenGetCategoryById_WhenCategoryDoesNotExist_ThenThrowResourceNotFoundException() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId)
        );

        assertEquals("Category not found with ID: " + categoryId, exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, never()).entityToResponse(any(Category.class));
    }


    @Test
    void givenDeleteCategoryById_WhenCategoryExists_ThenDeleteCategorySuccessfully() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(categoryId);


        categoryService.deleteCategoryById(categoryId);


        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void givenDeleteCategoryById_WhenCategoryDoesNotExist_ThenThrowResourceNotFoundException() {

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deleteCategoryById(categoryId)
        );

        assertEquals("Category not found with ID: " + categoryId + " for deleting", exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).deleteById(anyLong());
    }


    @Test
    void givenCountCategories_WhenInvoked_ThenReturnCorrectCategoryCount() {
        when(categoryRepository.count()).thenReturn(7L);


        Long count = categoryService.countCategories();


        assertEquals(7L, count);
        verify(categoryRepository, times(1)).count();
    }


}


