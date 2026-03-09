package com.team.project.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "공통 API 응답")
public class BaseResponse<T> {
	@Schema(description = "요청 성공 여부", example = "true")
	private final boolean success;
	@Schema(description = "응답 데이터 (성공 시 반환)")
	private final T data;
	@Schema(description = "에러 코드 (실패 시 반환)", nullable = true, example = "ERROR_CODE")
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