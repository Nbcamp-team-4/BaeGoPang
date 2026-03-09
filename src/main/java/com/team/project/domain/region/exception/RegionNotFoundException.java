package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class RegionNotFoundException extends BaseException {

	public RegionNotFoundException() {
		super(RegionErrorCode.REGION_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}