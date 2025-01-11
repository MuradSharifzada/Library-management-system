package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.CategoryRequest;
import com.librarymanagementsystem.dto.response.CategoryResponse;
import com.librarymanagementsystem.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    CategoryResponse entityToResponse(Category category);
    Category requestToEntity(CategoryRequest request);



}
