package com.librarymanagementsystem.exception;

import lombok.Getter;

@Getter
public class IllegalStateException extends RuntimeException {
    private final String message;

    public IllegalStateException(String message) {
        super(message);
        this.message = message;
    }
}
