package com._team._project.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class GloabalExceptionHandler {

    @Getter
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private int statusCode;
        private String message;
        private T data;

        public static <T> ApiResponse<T> ok(T data) {
            return new ApiResponse<>(200, "success", data);
        }
    }

}
