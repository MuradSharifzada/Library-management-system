package com.librarymanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of a category.")
public class CategoryResponse {

    @Schema(description = "The unique identifier of the category.", example = "1")
    Long id;

    @Schema(description = "The name of the category.", example = "Self-Help")
    String name;

    @Schema(description = "A description of the category.", example = "Books focused on personal development and growth.")
    String description;

    @Schema(description = "The type of category (e.g., Fiction, Non-Fiction).", example = "Non-Fiction")
    String type;
}
