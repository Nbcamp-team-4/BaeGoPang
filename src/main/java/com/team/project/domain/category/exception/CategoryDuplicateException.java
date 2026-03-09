package com.team.project.domain.category.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class CategoryDuplicateException extends BaseException {

	public CategoryDuplicateException() {
		super(CategoryErrorCode.CATEGORY_DUPLICATE.name(), HttpStatus.CONFLICT);
	}
}