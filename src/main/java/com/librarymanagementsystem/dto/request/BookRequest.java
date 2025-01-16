package com.librarymanagementsystem.dto.request;

import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.validation.UniqueTitle;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {

    @NotBlank(message = "Book name must not be blank.")
    @Size(max = 255, message = "Book name must not exceed 255 characters.")
    @UniqueTitle(entity = BookService.class, message = "Book name must be unique.")
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


    @Min(value = 0, message = "Stock count must not be negative.")
    int stockCount;


    @NotNull(message = "Authors ID list must not be null.")
    @NotEmpty(message = "Authors ID list must not be empty.")
    List<@Positive(message = "Author ID must be a positive number.") Long> authorId;
}
