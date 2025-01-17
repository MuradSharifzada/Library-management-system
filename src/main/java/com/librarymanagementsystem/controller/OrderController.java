package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.OrderRequest;
import com.librarymanagementsystem.dto.response.OrderResponse;
import com.librarymanagementsystem.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all orders", description = "Retrieves a paginated list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @Parameter(description = "Page number for pagination, defaults to 0", example = "0")
            @RequestParam(defaultValue = "0", name = "Page Number") int pageNumber,
            @Parameter(description = "Page size for pagination, defaults to 10", example = "10")
            @RequestParam(defaultValue = "10", name = "Page Size") int pageSize) {
        List<OrderResponse> orders = orderService.getAllOrders(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    @Operation(summary = "Borrow an order", description = "Creates new order for borrowing a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order borrowed successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details order to borrow",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"studentId\": 1, \"bookId\": 101}")))
            @Valid @RequestBody OrderRequest orderRequest) {
        orderService.borrowOrder(orderRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Order borrowed successfully");
    }

    @Operation(summary = "Return an order", description = "The return of a borrowed book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order returned successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/return")
    public ResponseEntity<String> returnOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the order to return",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"studentId\": 1, \"bookId\": 101}")))
            @Valid @RequestBody OrderRequest orderRequest) {
        orderService.returnOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Order returned successfully");
    }
}
