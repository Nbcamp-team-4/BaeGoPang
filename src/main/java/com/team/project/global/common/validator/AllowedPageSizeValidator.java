package com.team.project.global.common.validator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.team.project.global.common.annotation.AllowedPageSize;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedPageSizeValidator implements ConstraintValidator<AllowedPageSize, Integer> {

	private Set<Integer> allowedValues;

	@Override
	public void initialize(AllowedPageSize constraintAnnotation) {
		allowedValues = Arrays.stream(constraintAnnotation.values())
			.boxed()
			.collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null 허용 여부는 @NotNull로 별도 제어
		}
		return allowedValues.contains(value);
	}
}