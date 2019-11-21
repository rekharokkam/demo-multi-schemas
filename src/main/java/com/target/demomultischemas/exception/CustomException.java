package com.target.demomultischemas.exception;

public class CustomException extends RuntimeException{
    private String message;

    public CustomException(String message) {
        super (message);
    }
}
