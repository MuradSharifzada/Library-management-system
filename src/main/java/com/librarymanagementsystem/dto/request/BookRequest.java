package com.librarymanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.validation.UniqueTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an Book.")
public class BookRequest {

    @NotBlank(message = "Book name must not be blank.")
    @Size(max = 255, message = "Book name must not exceed 255 characters.")
    @UniqueTitle(entity = BookService.class, message = "Book name must be unique.")
    @Schema(description = "The name of the book.", example = "Atomic Habits")
    String name;

    @NotBlank(message = "ISBN must not be blank.")
    @Schema(description = "The International Standard Book Number of the book.", example = "9780735211292")
    String isbn;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    @Schema(description = "A brief description of the book.", example = "An easy and proven way to build good habits and break bad ones.")
    String description;

    @PastOrPresent(message = "Published date must be in the past or today.")
    @Schema(description = "The date when the book was published.", example = "2018-10-16")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate publishedTime;

    @NotNull(message = "Category ID must not be null.")
    @Positive(message = "Category ID must be a positive number.")
    @Schema(description = "The ID of the category to which the book belongs.", example = "2")
    Long categoryId;

    @Min(value = 0, message = "Stock count must not be negative.")
    @Schema(description = "The number of copies available in stock.", example = "50")
    int stockCount;

    @Schema(description = "The name of the image file stored in the cloud.", example = "atomic_habits.jpg")
    String bookImage;

    @NotNull(message = "Authors ID list must not be null.")
    @NotEmpty(message = "Authors ID list must not be empty.")
    @Schema(description = "The list of IDs for the authors of the book.", example = "[1]")
    List<@Positive(message = "Author ID must be a positive number.") Long> authorId;
}
