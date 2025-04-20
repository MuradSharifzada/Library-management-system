package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.model.entity.Order;
import com.librarymanagementsystem.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static java.time.LocalDateTime.now;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    Order requestToEntity(OrderRequest request);

    @Mapping(target = "book.categoryId", ignore = true)
    @Mapping(target = "book.authorIds", ignore = true)
    OrderResponse entityToResponse(Order order);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "BORROWED")
    @Mapping(target = "returnDate", ignore = true)
    Order toOrder(OrderRequest request, Book book, Student student);


}
