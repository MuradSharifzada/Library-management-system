package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Category;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapperHelper {

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
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("mapAuthors")
    public List<Author> mapAuthors(List<Long> authorIds) {
        return authorIds.stream()
                .map(id -> {
                    Author author = new Author();
                    author.setId(id);
                    return author;
                }).collect(Collectors.toList());
    }

    @Named("mapAuthorsToIds")
    public List<Long> mapAuthorsToIds(List<Author> authors) {
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toList());
    }

}