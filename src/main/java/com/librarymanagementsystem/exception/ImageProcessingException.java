package com.librarymanagementsystem.exception;

public class ImageProcessingException extends RuntimeException{
    private final String message;

    public ImageProcessingException(String message){
        super(message);
        this.message=message;
    }
}
