package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Category;
import com.librarymanagementsystem.repository.AuthorRepository;
import com.librarymanagementsystem.repository.CategoryRepository;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapperHelper {


    private final CategoryRepository categoryRepository;

    private final AuthorRepository authorRepository;

    @Named("mapAuthorsToNames")
    public List<String> mapAuthorsToNames(List<Author> authors) {
        return authors.stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .collect(Collectors.toList());
    }

    @Named("mapCategoryToName")
    public String mapCategoryToName(Category category) {
        return category != null ? category.getName() : "Unknown Category";
    }

    @Named("mapCategory")
    public Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    @Named("mapAuthors")
    public List<Author> mapAuthors(List<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) return List.of();
        return authorRepository.findAllById(authorIds);
    }

    @Named("mapAuthorsToIds")
    public List<Long> mapAuthorsToIds(List<Author> authors) {
        return authors != null ? authors.stream().map(Author::getId).collect(Collectors.toList()) : Collections.emptyList();
    }
}
