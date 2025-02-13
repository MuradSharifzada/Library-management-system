package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.AuthorRequest;
import com.librarymanagementsystem.dto.response.AuthorResponse;
import com.librarymanagementsystem.model.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    AuthorResponse entityToResponse(Author author);


    @Mappings(value = {
            @Mapping(target = "books", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Author requestToEntity(AuthorRequest request);
}
