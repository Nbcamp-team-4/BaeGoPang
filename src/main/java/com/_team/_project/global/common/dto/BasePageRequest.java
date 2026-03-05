package com._team._project.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasePageRequest {
	private Integer page = 0;
	private Integer size = 10;
}
