package com.team.project.domain.payment_log.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.AllArgsConstructor;
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
			.map((PaymentLog content) -> new Item(content.getId(), content.getStatus(), content.getReason(),
				content.getPayment().getId(), content.getPayment().getStatus(), content.getPayment().getAmount(),
				content.getPayment().getPaidAt()))
			.toList();
		return new GetPaymentLogsQuery(contents, pageResult.getNumber(), pageResult.getSize(),
			pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Data
	@AllArgsConstructor
	static public class Item {
		private UUID pamentLogid;
		private PaymentLogStatus paymentLogStatus;
		private String paymentLogReason;
		private UUID paymentId;
		private PaymentStatus paymentStatus;
		private Integer paymentAmount;
		private LocalDateTime paidAt;
	}
}


