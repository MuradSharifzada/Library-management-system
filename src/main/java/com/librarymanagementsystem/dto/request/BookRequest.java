package com.librarymanagementsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {

    @NotBlank(message = "Book name must not be blank.")
    @Size(max = 255, message = "Book name must not exceed 255 characters.")
    String name;

    @NotBlank(message = "ISBN must not be blank.")
    String isbn;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    String description;

    @PastOrPresent(message = "Published date must be in the past or today.")
    LocalDate publishedTime;

    @NotNull(message = "Category ID must not be null.")
    @Positive(message = "Category ID must be a positive number.")
    Long categoryId;

    MultipartFile bookImage;

    @Min(value = 0, message = "Stock count must not be negative.")
    int stockCount;

    @NotEmpty(message = "Authors list must not be empty.")
    private List<@Valid AuthorRequest> authors;
}
