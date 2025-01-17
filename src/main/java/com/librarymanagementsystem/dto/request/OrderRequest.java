package com.librarymanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an order.")
public class OrderRequest {

    @NotNull(message = "Student ID must not be null.")
    @Schema(description = "The unique ID of the student placing the order.", example = "1")
    Long studentId;

    @NotNull(message = "Book ID must not be null.")
    @Schema(description = "The unique ID of the book being ordered.", example = "101")
    Long bookId;
}