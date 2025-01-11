package com.librarymanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ResourceNotFoundException.class,
            ResourceAlreadyExistsException.class,
            InsufficientCountException.class
    })

    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDate(LocalDateTime.now());
        errorResponse.setStatus(getHttpStatus(ex));
        errorResponse.setErrorCode(errorResponse.getStatus().value());
        errorResponse.setErrorMessage(ex.getMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    private HttpStatus getHttpStatus(Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof ResourceAlreadyExistsException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof InsufficientCountException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
