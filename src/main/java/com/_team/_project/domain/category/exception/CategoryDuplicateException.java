package com._team._project.domain.category.exception;

public class CategoryDuplicateException extends RuntimeException {
	public CategoryDuplicateException() {
		super("Category_Duplicate");
	}
}