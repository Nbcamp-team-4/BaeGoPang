package com.team.project.domain.category.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class CategoryNotFoundException extends BaseException {
	public CategoryNotFoundException() {
		super("Category_Not_Found", HttpStatus.NOT_FOUND);
	}
}