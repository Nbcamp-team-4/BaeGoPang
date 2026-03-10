package com.team.project.domain.region.api.request;

import com.team.project.global.common.dto.BasePageRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionSearchRequest extends BasePageRequest {

	@Schema(description = "지역명 검색어", example = "광화문")
	private String keyword;
}