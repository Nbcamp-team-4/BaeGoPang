package com.team.project.global.common.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "페이지 기반 응답 공통 구조")
public class BasePageResponse<T> {
	@Schema(description = "조회된 데이터 목록")
	private List<T> content;

	@Schema(description = "현재 페이지 번호", example = "0")
	private Integer page;

	@Schema(description = "페이지 크기", example = "10")
	private Integer size;

	@Schema(description = "전체 데이터 개수", example = "125")
	private Long totalElements;
	
	@Schema(description = "전체 페이지 수", example = "13")
	private Integer totalPages;
}
