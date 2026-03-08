package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class RegionNotFoundException extends BaseException {
	public RegionNotFoundException() {
		super("REGION_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}