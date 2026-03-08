package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetPaymentsQuery extends BasePageResponse<GetPaymentsQuery.Item> {

	public GetPaymentsQuery(List<GetPaymentsQuery.Item> content, Integer page, Integer size, Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentsQuery from(Page<Payment> pageResult) {
		List<GetPaymentsQuery.Item> contents = pageResult.getContent()
			.stream()
			.map((Payment content) -> GetPaymentsQuery.Item.builder().build())
			.toList();
		return new GetPaymentsQuery(contents, pageResult.getNumber(), pageResult.getSize(),
			pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
		private PaymentStatus status;
		private Integer amount;
		private String pamentKey;
		private LocalDateTime paidAt;
		private UUID orderId;
		private UUID orderNo;
	}
}
