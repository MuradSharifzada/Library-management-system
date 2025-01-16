package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders(int pageNumber, int size);

    void borrowOrder(OrderRequest request);

    void returnOrder(OrderRequest orderRequest);
}
