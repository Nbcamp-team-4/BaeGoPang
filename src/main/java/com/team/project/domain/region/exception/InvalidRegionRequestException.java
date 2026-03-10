package com.team.project.domain.region.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidRegionRequestException extends BaseException {

    public InvalidRegionRequestException() {
        super(RegionErrorCode.INVALID_REGION_REQUEST.name(), HttpStatus.BAD_REQUEST);
    }
}