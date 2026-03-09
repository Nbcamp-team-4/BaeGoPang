package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidRegionGeomException extends BaseException {

	public InvalidRegionGeomException() {
		super(RegionErrorCode.INVALID_REGION_GEOM.name(), HttpStatus.BAD_REQUEST);
	}
}