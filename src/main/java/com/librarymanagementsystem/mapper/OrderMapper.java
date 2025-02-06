package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "returnDate", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "orderDate", ignore = true)
//    @Mapping(target = "student", ignore = true)
//    @Mapping(target = "book", ignore = true)
    Order requestToEntity(OrderRequest request);

    @Mapping(target = "book.categoryId", ignore = true)
    @Mapping(target = "book.authorIds", ignore = true)
    OrderResponse entityToResponse(Order order);
}
