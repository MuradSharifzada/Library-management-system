package com.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorRequest {

    @NotBlank(message = "First name must not be empty.")
    @Size(max = 25, min = 2, message = "Invalid first name: Length must be between 2 and 25 characters.")
    String firstName;

    @NotBlank(message = "Last name must not be empty.")
    @Size(max = 30, min = 3, message = "Invalid last name: Length must be between 3 and 30 characters.")
    String lastName;

    @Past(message = "Birth date must not be in the future.")
    LocalDate birthDay;

    @Size(max = 200, message = "Invalid biography: Length must not exceed 200 characters.")
    String biography;

}
