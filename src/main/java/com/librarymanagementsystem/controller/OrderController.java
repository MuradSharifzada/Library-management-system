package com.librarymanagementsystem.controller;


import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0",name = "Page Number") int pageNumber,
            @RequestParam(defaultValue = "10",name = "Page Size") int pageSize) {
        List<OrderResponse> orders = orderService.getAllOrders(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }


    @PostMapping("/borrow")
    public ResponseEntity<String> borrowOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.borrowOrder(orderRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Order borrowed successfully");
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.returnOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Order returned successfully");
    }

}
