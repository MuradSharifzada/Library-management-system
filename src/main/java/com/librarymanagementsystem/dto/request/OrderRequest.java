package com.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotNull(message = "Student ID must not be null.")
    Long studentId;

    @NotNull(message = "Book ID must not be null.")
    Long bookId;

    @NotNull(message = "Return date must not be null.")
    LocalDateTime returnDate;
}
