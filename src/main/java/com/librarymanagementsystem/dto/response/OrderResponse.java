package com.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.librarymanagementsystem.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an order.")
public class OrderResponse {

    @Schema(description = "The unique identifier of the order.", example = "1")
    Long id;

    @Schema(description = "Details of the student associated with the order.")
    StudentResponse student;

    @Schema(description = "Details of the book associated with the order.")
    BookResponse book;

    @Schema(description = "The date and time when the order was placed.", example = "2025-01-06T10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime orderDate;

    @Schema(description = "The date and time by which the order must be returned.", example = "2025-01-06T10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime returnDate;

    @Schema(description = "The current status of the order.", example = "BORROWED")
    Status status=Status.BORROWED;


}