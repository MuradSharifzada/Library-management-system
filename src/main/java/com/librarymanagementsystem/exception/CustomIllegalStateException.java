package com.librarymanagementsystem.exception;

import lombok.Getter;

@Getter
public class CustomIllegalStateException extends RuntimeException {
    private final String message;

    public CustomIllegalStateException(String message) {
        super(message);
        this.message = message;
    }
}
