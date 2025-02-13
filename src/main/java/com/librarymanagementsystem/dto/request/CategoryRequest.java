package com.librarymanagementsystem.dto.request;

import com.librarymanagementsystem.service.CategoryService;
import com.librarymanagementsystem.validation.UniqueTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an category.")
@Builder

public class CategoryRequest {

    @NotBlank(message = "Category name must not be blank.")
    @Size(max = 50, message = "Category name must not exceed 50 characters.")
    @UniqueTitle(entity = CategoryService.class, message = "Category name must be unique.",groups = Create.class)
    @Schema(description = "Name of the category", example = "Self-Help")
    String name;

    @Size(max = 200, message = "Description must not exceed 200 characters.")
    @Schema(description = "Description of the category", example = "Books focused on personal development and growth")
    String description;

    @NotBlank(message = "Category type must not be blank.")
    @Size(max = 30, message = "Category type must not exceed 30 characters.")
    @Schema(description = "Type of the category", example = "Non-Fiction")
    String type;

    public interface Create{}
    public interface Update{}
}
