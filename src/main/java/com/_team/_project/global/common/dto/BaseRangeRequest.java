package com._team._project.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseRangeRequest<T> {
	T min;
	T max;
}
