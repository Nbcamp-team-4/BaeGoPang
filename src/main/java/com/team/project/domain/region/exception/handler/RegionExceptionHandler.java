package com.team.project.domain.region.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.project.domain.region.exception.RegionNotFoundException;

@RestControllerAdvice
public class RegionExceptionHandler {

    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<String> handleRegionNotFound(RegionNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}