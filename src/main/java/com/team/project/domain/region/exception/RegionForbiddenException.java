package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class RegionForbiddenException extends BaseException {

	public RegionForbiddenException() {
		super(RegionErrorCode.REGION_FORBIDDEN.name(), HttpStatus.FORBIDDEN);
	}
}