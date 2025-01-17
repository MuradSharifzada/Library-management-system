package com.librarymanagementsystem.exception;

import lombok.Getter;

@Getter
public class AmazonServiceException extends RuntimeException {

    private final String message;

    public AmazonServiceException(String message) {
        super(message);
        this.message = message;
    }
}
