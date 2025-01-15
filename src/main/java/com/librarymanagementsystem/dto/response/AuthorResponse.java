package com.librarymanagementsystem.dto.response;

import com.librarymanagementsystem.model.entity.Book;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorResponse {

    Long id;

    String firstName;

    String lastName;

    LocalDate birthDay;

    String biography;

    Set<BookResponse> books;


}
