package com.team.project.domain.user.exception;

import com.team.project.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailDuplicateException extends BaseException {
	public EmailDuplicateException() {
		super("EMAIL_DUPLICATED", HttpStatus.NOT_FOUND);
	}
}
