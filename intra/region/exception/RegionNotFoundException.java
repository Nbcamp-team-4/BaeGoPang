package com.team.project.domain.region.exception;

public class RegionNotFoundException extends RuntimeException {
    public RegionNotFoundException() {
        super("해당 지역을 찾을 수 없습니다.");
    }
}