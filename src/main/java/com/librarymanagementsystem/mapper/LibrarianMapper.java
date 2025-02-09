package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.LibrarianRequest;
import com.librarymanagementsystem.dto.response.LibrarianResponse;
import com.librarymanagementsystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibrarianMapper {

    User requestToEntity(LibrarianRequest request);

    LibrarianResponse entityToResponse(User librarian);
}
