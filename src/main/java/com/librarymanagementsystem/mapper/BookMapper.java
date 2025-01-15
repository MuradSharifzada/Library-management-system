package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookMapperHelper.class})
public interface BookMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "authors", target = "authorId", qualifiedByName = "mapAuthorsToIds")
    BookResponse entityToResponse(Book book);

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
    @Mapping(source = "authorId", target = "authors", qualifiedByName = "mapAuthors")
    @Mapping(target = "orders", ignore = true)
    Book requestToEntity(BookRequest request);
}
