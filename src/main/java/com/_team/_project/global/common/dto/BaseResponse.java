package com._team._project.global.common.dto;

import lombok.Getter;

@Getter
public class BaseResponse<T> {

	private final boolean success;
	private final T data;
	private final String errorCode;

	private BaseResponse(boolean success, T data, String errorCode) {
		this.success = success;
		this.data = data;
		this.errorCode = errorCode;
	}

	public static <T> BaseResponse<T> ofSuccess(T data) {
		return new BaseResponse<>(true, data, null);
	}

	public static <T> BaseResponse<T> ofError(String errorCode) {
		return new BaseResponse<>(false, null, errorCode);
	}
}