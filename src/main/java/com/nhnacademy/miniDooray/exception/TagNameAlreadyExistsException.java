package com.nhnacademy.miniDooray.exception;

public class TagNameAlreadyExistsException extends RuntimeException {
    public TagNameAlreadyExistsException(String message) {
        super(message);
    }
}
