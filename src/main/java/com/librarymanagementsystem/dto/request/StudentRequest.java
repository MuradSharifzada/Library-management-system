package com.librarymanagementsystem.dto.request;

import com.librarymanagementsystem.service.StudentService;
import com.librarymanagementsystem.validation.UniqueTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of a student.")
public class StudentRequest {

    @NotBlank(message = "Student FIN must not be blank.")
    @Size(min = 7, message = "Student FIN must be at least 7 characters long.")
    @UniqueTitle(entity = StudentService.class, message = "Student FIN must be unique.")
    @Schema(description = "FIN", example = "FIN1234")
    String FIN;

    @NotBlank(message = "First name must not be blank.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    @Schema(description = "The first name of the student.", example = "Murad")
    String firstName;

    @NotBlank(message = "Last name must not be blank.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    @Schema(description = "The last name of the student.", example = "Sharifzada")
    String lastName;

    @NotNull(message = "Email address must not be null.")
    @Email(message = "Email must be a valid Gmail address.")
    @UniqueTitle(entity = StudentService.class, message = "Student Email must be unique.")
    @Schema(description = "The email address of the student. Must be unique and in Gmail format.", example = "muradsharifzada@gmail.com")
    String email;

    @NotBlank(message = "Phone number must not be blank.")
    @Schema(description = "The phone number of the student.", example = "+987654321")
    String phoneNumber;

    @NotBlank(message = "Student group must not be blank.")
    @Schema(description = "The group to which the student belongs.", example = "232K eng")
    String studentGroup;

    @NotNull(message = "Birth date must not be null.")
    @Past(message = "Birth date must be a past date.")
    @Schema(description = "The birth date of the student. Must be a past date.", example = "2005-01-01")
    LocalDate birthDate;
}
