package com.librarymanagementsystem.exception;

import lombok.Getter;

@Getter
public class InsufficientCountException extends RuntimeException {
    private final String message;

    public InsufficientCountException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
