package com.team.project.domain.payment.api.request;

import java.time.LocalDateTime;

import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentsRequest {
	private Integer page;
	private Integer size;
	private BaseRangeRequest<LocalDateTime> rangePaidAt;

}
