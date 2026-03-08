package com._team._project.domain.user.exception;

public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException() {
        super("EMAIL_DUPLICATED");
    }
}
