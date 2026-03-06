package com.team.project.domain.category.exception;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException() {
		super("Category_Not_Found");
	}
}