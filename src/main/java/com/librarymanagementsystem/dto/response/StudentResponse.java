package com.librarymanagementsystem.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {

    String FIN;

    String first_name;

    String email;

    String lastName;

    String studentGroup;

    LocalDate birthDate;


}
