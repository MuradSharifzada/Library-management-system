package com.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of a book.")
@Builder
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


    @Schema(description = "The category ID of the book.", example = "2")
    Long categoryId;

    @Schema(description = "The name of the image file stored in the cloud.", example = "atomic_habits.jpg")
    String bookImage;

    @Schema(description = "The date when the book was published.", example = "2018-10-16")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate publishedTime;

    String categoryName;

    @Schema(description = "List of author IDs.", example = "[1, 3]")
    List<Long> authorIds;


}
