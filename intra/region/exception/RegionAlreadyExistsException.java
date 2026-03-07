package com.team.project.domain.region.exception;

public class RegionAlreadyExistsException extends RuntimeException {
    public RegionAlreadyExistsException() {
        super("이미 존재하는 지역명입니다.");
    }
}