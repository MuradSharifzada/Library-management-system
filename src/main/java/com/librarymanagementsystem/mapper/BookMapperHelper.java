package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Category;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapperHelper {

    @Named("mapAuthorsToIds")
    public List<Long> mapAuthorsToIds(List<Author> authors) {
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toList());
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
}
