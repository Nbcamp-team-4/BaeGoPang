package com.team.project.domain.category.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends BaseException {

	public CategoryNotFoundException() {
		super(CategoryErrorCode.CATEGORY_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}