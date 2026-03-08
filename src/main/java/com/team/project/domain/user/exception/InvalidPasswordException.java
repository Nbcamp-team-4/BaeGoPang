package com._team._project.domain.user.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("INVALID_PASSWORD");
    }
}
