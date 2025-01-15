package com.librarymanagementsystem.exception;

import lombok.Getter;

@Getter
public class InsufficientCountException extends RuntimeException {
    private final String message;

    public InsufficientCountException(String message) {
        super(message);
        this.message = message;
    }
}
