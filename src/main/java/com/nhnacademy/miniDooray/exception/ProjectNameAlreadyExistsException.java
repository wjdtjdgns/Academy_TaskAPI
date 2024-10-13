package com.nhnacademy.miniDooray.exception;

public class ProjectNameAlreadyExistsException extends RuntimeException {
    public ProjectNameAlreadyExistsException(String message) {
        super(message);
    }
}
