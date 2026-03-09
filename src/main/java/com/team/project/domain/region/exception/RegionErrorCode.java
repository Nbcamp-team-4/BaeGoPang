package com.team.project.domain.region.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionErrorCode {

	INVALID_REGION_REQUEST("요청 값이 올바르지 않습니다."),
	INVALID_REGION_GEOM("geomWkt 형식이 올바르지 않습니다."),
	REGION_NOT_FOUND("지역을 찾을 수 없습니다."),
	REGION_FORBIDDEN("해당 지역에 대한 권한이 없습니다.");

	private final String message;
}