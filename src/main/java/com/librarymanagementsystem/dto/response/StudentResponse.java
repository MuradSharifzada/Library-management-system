package com.librarymanagementsystem.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {

    Long id;

    String FIN;

    String firstName;

    String email;

    String lastName;

    String studentGroup;

    LocalDate birthDate;

    LocalDate enrollmentDate;


}
