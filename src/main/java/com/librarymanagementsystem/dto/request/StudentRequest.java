package com.librarymanagementsystem.dto.request;

import com.librarymanagementsystem.service.StudentService;
import com.librarymanagementsystem.validation.UniqueTitle;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRequest {

    @NotBlank(message = "Student FIN must not be blank.")
    @Size(min = 7, message = "Student FIN must be at least 7 characters long.")
    @UniqueTitle(entity = StudentService.class, message = "Student FIN  must be unique.")
    String FIN;

    @NotBlank(message = "First name must not be blank.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    String firstName;

    @NotBlank(message = "Last name must not be blank.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    String lastName;

    @NotNull(message = "Email address must not be null.")
    @Email(message = "Email must be a valid Gmail address.")
    String email;

    @NotBlank(message = "Phone number must not be blank.")
    String phoneNumber;

    @NotBlank(message = "Student group must not be blank.")
    String studentGroup;

    @NotNull(message = "Birth date must not be null.")
    @Past(message = "Birth date must be a past date.")
    LocalDate birthDate;
}
