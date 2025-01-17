package com.librarymanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of a student.")
public class StudentResponse {

    @Schema(description = "The unique identifier of the student.", example = "1")
    Long id;

    @Schema(description = "The unique (FIN) of the student.", example = "FIN98765")
    String FIN;

    @Schema(description = "The first name of the student.", example = "Murad")
    String firstName;

    @Schema(description = "The email address of the student.", example = "muradsharifzada@gmail.com")
    String email;

    @Schema(description = "The last name of the student.", example = "Sharifzada")
    String lastName;

    @Schema(description = "The group to which the student belongs.", example = "232K eng")
    String studentGroup;

    @Schema(description = "The birth date of the student.", example = "2005-01-01")
    LocalDate birthDate;

    @Schema(description = "The enrollment date of the student in the system.", example = "2025-01-06")
    LocalDate enrollmentDate;
}
