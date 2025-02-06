//package com.librarymanagementsystem.mapper;
//
//import com.librarymanagementsystem.dto.request.BookRequest;
//import com.librarymanagementsystem.dto.response.BookResponse;
//import com.librarymanagementsystem.model.entity.Book;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//
//@Mapper(componentModel = "spring", uses = {BookMapperHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface BookMapper {
//
//    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "authors", target = "authorIds", qualifiedByName = "mapAuthorsToIds")
//    BookResponse entityToResponse(Book book);
//
//    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
//    @Mapping(source = "authorIds", target = "authors", qualifiedByName = "mapAuthors")
//    @Mapping(target = "orders", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    Book requestToEntity(BookRequest request);
//}
//


package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.dto.response.BookResponse;
import com.librarymanagementsystem.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {BookMapperHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "authors", target = "authorIds", qualifiedByName = "mapAuthorsToIds")
    BookResponse entityToResponse(Book book);

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
    @Mapping(source = "authorIds", target = "authors", qualifiedByName = "mapAuthors")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "id", ignore = true)
    Book requestToEntity(BookRequest request);
}
