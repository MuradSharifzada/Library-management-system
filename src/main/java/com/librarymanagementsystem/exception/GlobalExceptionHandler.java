package com.librarymanagementsystem.exception;

import com.librarymanagementsystem.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ResourceNotFoundException.class,
            ResourceAlreadyExistsException.class,
            InsufficientCountException.class,
            IllegalStateException.class
    })

    public ResponseEntity<ErrorResponse> handleCustomExceptions(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .date(LocalDateTime.now())
                .status(getHttpStatus(ex))
                .errorCode(getHttpStatus(ex).value())
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse();
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Validation Error");
        error.setErrorMessage(errorMessage);
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST);
        log.error("Validation failed for argument: " + errorMessage);
        return error;
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
