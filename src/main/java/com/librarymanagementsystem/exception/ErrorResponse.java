package com.librarymanagementsystem.exception;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    HttpStatus status;
    LocalDateTime date;
    String errorMessage;
    int errorCode;

}
