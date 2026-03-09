package com.team.project.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "범위 검색 요청")
public class BaseRangeRequest<T> {

	@Schema(description = "최소 값")
	T min;

	@Schema(description = "최대 값")
	T max;
}
