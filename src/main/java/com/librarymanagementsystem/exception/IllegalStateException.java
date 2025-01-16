package com.librarymanagementsystem.exception;


public class IllegalStateException extends RuntimeException {
    private final String message;

    public IllegalStateException(String message) {
        super(message);
        this.message = message;
    }
}
