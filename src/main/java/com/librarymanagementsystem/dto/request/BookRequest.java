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
@Schema(description = "Represents the details of a Book.")
public class BookRequest {


    @Size(max = 255, message = "Book name must not exceed 255 characters.")
    @Schema(description = "The name of the book.", example = "Atomic Habits")
    @NotBlank(groups = Create.class)
    @UniqueTitle(entity = BookService.class, message = "Book name must be unique.",groups = Create.class)
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

    @Schema(description = "The category ID of the book.", example = "2")
    Long categoryId;

    @Min(value = 0, message = "Stock count must not be negative.")
    @Schema(description = "The number of copies available in stock.", example = "50")
    int stockCount;

    @Schema(description = "The name of the image file stored in the cloud.", example = "atomic_habits.jpg")
    String bookImage;

    @Schema(description = "The list of author IDs for the book.", example = "[1, 3]")
    List<Long> authorIds;

    public interface Create {}
    public interface Update {}
}
