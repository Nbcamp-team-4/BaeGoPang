package com._team._project.domain.user.exception;

public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException() {
        super("USER_DUPLICATED");
    }
}
