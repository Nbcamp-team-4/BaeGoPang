package com.team.project.domain.category.api.request;

import com.team.project.global.common.dto.BasePageRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CategoryPageRequest extends BasePageRequest {

	@Schema(description = "카테고리명 검색", example = "치킨")
	private String name;
}