package com.librarymanagementsystem.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {

    Long id;
    String name;

    String description;

    String isbn;

    int stockCount;


    LocalDate publishedTime;


    Long categoryId;


    List<Long> authorId;




}
