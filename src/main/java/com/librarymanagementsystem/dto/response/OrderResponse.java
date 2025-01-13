package com.librarymanagementsystem.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

     Long id;

     StudentResponse student;

     BookResponse book;

     LocalDateTime orderDate;

     LocalDateTime returnDate;

     String status;

}
