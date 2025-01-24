package com.librarymanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an Author.")
public class AuthorRequest {

    @NotBlank(message = "First name must not be empty.")
    @Size(max = 25, min = 2, message = "Invalid first name: Length must be between 2 and 25 characters.")
    @Schema(description = "The first name of the author.", example = "James")
    String firstName;

    @NotBlank(message = "Last name must not be empty.")
    @Size(max = 30, min = 3, message = "Invalid last name: Length must be between 3 and 30 characters.")
    @Schema(description = "The last name of the author.", example = "Clear")
    String lastName;

    @Past(message = "Birth date must not be in the future.")
    @Schema(description = "The birth date of the author.", example = "1986-01-22")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDay;

    @Size(max = 200, message = "Invalid biography: Length must not exceed 200 characters.")
    @Schema(description = "A brief biography of the author.", example = "James Clear is the author of Atomic Habits, a bestseller on building good habits.")
    String biography;

}