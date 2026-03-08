package com.team.project.domain.user.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.project.domain.user.exception.EmailDuplicateException;
import com.team.project.domain.user.exception.InvalidPasswordException;
import com.team.project.domain.user.exception.UserDuplicateException;
import com.team.project.domain.user.exception.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleNotFound(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(UserDuplicateException.class)
	public ResponseEntity<String> handleDuplicate(UserDuplicateException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(EmailDuplicateException.class)
	public ResponseEntity<String> handleDuplicate(EmailDuplicateException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<String> handleDuplicate(InvalidPasswordException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
}
