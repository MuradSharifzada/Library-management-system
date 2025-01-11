package com.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    private final String message;

    public ResourceAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
