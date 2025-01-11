package com.librarymanagementsystem.dto.response;

import com.librarymanagementsystem.model.entity.Book;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data

public class AuthorResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDay;

    private String biography;

    private Set<Book> books;


}
