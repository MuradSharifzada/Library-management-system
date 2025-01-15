package com.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;



@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotNull(message = "Student ID must not be null.")
    Long studentId;

    @NotNull(message = "Book ID must not be null.")
    Long bookId;
}
