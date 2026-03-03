package com._team._project.global.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasePageResponse<T> {
	private List<T> content;
	private Integer page;
	private Integer size;
	private Long totalElements;
	private Integer totalPages;
}
