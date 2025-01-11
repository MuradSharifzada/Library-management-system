package com.librarymanagementsystem.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    HttpStatus status;
    LocalDateTime date;
    String errorMessage;
    int errorCode;
}
