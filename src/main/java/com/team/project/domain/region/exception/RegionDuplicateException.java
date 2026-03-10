package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class RegionDuplicateException extends BaseException {

	public RegionDuplicateException() {
		super(RegionErrorCode.REGION_DUPLICATE.name(), HttpStatus.CONFLICT);
	}
}