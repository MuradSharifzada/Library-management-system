package com.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {

    @NotBlank(message = "Category name must not be blank.")
    @Size(max = 50, message = "Category name must not exceed 50 characters.")
    String name;

    @Size(max = 200, message = "Description must not exceed 200 characters.")
    String description;

    @NotBlank(message = "Category type must not be blank.")
    @Size(max = 30, message = "Category type must not exceed 30 characters.")
    String type;
}
