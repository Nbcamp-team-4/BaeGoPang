package com.team.project.domain.payment.infrastructure.gateway.toss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossCancelRequest {
	private String cancelReason;
}
