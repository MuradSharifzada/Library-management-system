package com.librarymanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of a book.")
public class BookResponse {

    @Schema(description = "The unique identifier of the book.", example = "1")
    Long id;

    @Schema(description = "The name of the book.", example = "Atomic Habits")
    String name;

    @Schema(description = "Description of the book.", example = "An easy and proven way to build good habits and break bad ones.")
    String description;

    @Schema(description = "The (ISBN) of the book.", example = "9780735211292")
    String isbn;

    @Schema(description = "The number of copies available in stock.", example = "50")
    int stockCount;

    @Schema(description = "The date when the book was published.", example = "2018-10-16")
    LocalDate publishedTime;

    @Schema(description = "The ID of the category to which the book belongs.", example = "2")
    Long categoryId;

    @Schema(description = "A list of IDs for the authors of the book.", example = "[1]")
    List<Long> authorId;

}
