package com.team.project.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public abstract class BasePageRequest {

	@Schema(description = "페이지 번호", example = "0", defaultValue = "0")
	protected Integer page = 0;

	@Schema(
		description = "페이지 크기",
		example = "10",
		defaultValue = "10",
		allowableValues = {"10", "30", "50"}
	)
	protected Integer size = 10;

	public Integer getPage() {
		if (page == null || page < 0) {
			return 0;
		}
		return page;
	}

	public Integer getSize() {
		if (size == null) {
			return 10;
		}
		return switch (size) {
			case 10, 30, 50 -> size;
			default -> 10;
		};
	}
}