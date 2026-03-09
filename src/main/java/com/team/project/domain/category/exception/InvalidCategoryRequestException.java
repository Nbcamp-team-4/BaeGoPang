package com.team.project.domain.category.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidCategoryRequestException extends BaseException {

	public InvalidCategoryRequestException() {
		super(CategoryErrorCode.INVALID_CATEGORY_REQUEST.name(), HttpStatus.BAD_REQUEST);
	}
}