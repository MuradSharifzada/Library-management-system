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

    @NotBlank(message = "First Name cannot be empty")
    @Size(max = 25, min = 2, message = "Invalid First Name: Must be of 2 - 25 characters")
    String firstName;


    @NotBlank(message = "Last Name cannot be empty")
    @Size(max = 25, message = "Invalid Last Name: Must be of 3 - 30 characters")
    String lastName;

    @Past(message = "Date should not be in the future")
    LocalDate birthDay;

    @Size(max = 200, message = "Invalid biography: Must be of max 200 characters")
    String biography;


}
