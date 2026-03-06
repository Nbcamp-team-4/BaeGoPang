package com.team.project.domain.payment_log.model.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetPaymentLogsQuery extends BasePageResponse<GetPaymentLogsQuery.Item> {

	public GetPaymentLogsQuery(List<Item> content, Integer page, Integer size, Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentLogsQuery from(Page<PaymentLog> pageResult) {
		List<GetPaymentLogsQuery.Item> contents = pageResult.getContent()
			.stream()
			.map((PaymentLog content) -> GetPaymentLogsQuery.Item.builder().build())
			.toList();
		return new GetPaymentLogsQuery(contents, pageResult.getNumber(), pageResult.getSize(),
			pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
	}
}


