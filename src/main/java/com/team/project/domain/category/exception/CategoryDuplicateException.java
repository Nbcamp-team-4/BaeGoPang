package com.team.project.domain.category.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class CategoryDuplicateException extends BaseException {
	public CategoryDuplicateException() {
		super("Category_Duplicate", HttpStatus.CONFLICT);
	}
}