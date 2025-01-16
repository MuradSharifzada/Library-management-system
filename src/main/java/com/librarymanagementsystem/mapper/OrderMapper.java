package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface OrderMapper {

    @Mapping(target = "returnDate", ignore = true)
    Order requestToEntity(OrderRequest request);

    OrderResponse entityToResponse(Order order);
}
