package com.librarymanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.ui.Model;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            ResourceAlreadyExistsException.class,
            InsufficientCountException.class,
            CustomIllegalStateException.class
    })
    public String handleCustomExceptions(Exception ex, Model model) {
        log.error("Custom Exception Caught: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", getFriendlyTitle(ex));
        model.addAttribute("errorMessage", getFriendlyMessage(ex));
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input data. Please check your form.");
        log.error("Validation Error: {}", errorMessage, ex);
        model.addAttribute("errorTitle", "Validation Issue");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex, Model model) {
        log.error("IO Exception Occurred: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "System Error");
        model.addAttribute("errorMessage", "Something went wrong while processing your request. Please try again later.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "An Unexpected Error Occurred");
        model.addAttribute("errorMessage", "We encountered an issue. Please try again or contact support.");
        return "error";
    }

    private String getFriendlyTitle(Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            return "Oops! We couldn't find that";
        } else if (ex instanceof ResourceAlreadyExistsException) {
            return "This item already exists!";
        } else if (ex instanceof InsufficientCountException) {
            return "Not Enough Stock Available";
        } else if (ex instanceof CustomIllegalStateException) {
            return "Action Not Allowed";
        }
        return "Something went wrong!";
    }

    private String getFriendlyMessage(Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            return "We couldn't find what you're looking for. It might have been moved or deleted.";
        } else if (ex instanceof ResourceAlreadyExistsException) {
            return "This item already exists in our system. Try using a different one.";
        } else if (ex instanceof InsufficientCountException) {
            return "Oops! We don’t have enough stock available for your request. Please try again with fewer items.";
        } else if (ex instanceof CustomIllegalStateException) {
            return "You have already borrowed this book and need to return it before borrowing again.";
        }
        return "Something went wrong on our end. Please refresh the page or try again later. If the issue persists, contact support.";
    }

}
