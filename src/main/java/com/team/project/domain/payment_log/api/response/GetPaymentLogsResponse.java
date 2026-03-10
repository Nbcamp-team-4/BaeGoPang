package com.team.project.domain.payment_log.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "결제 목록 조회 응답")
@Getter
public class GetPaymentLogsResponse extends BasePageResponse<GetPaymentLogsResponse.Item> {
	public GetPaymentLogsResponse(List<Item> content, Integer page, Integer size, Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentLogsResponse from(GetPaymentLogsQuery query) {

		List<Item> items = query.getContent()
			.stream()
			.map(item -> new Item(item.getPamentLogid(), item.getPaymentLogStatus(), item.getPaymentLogReason(),
				item.getPaymentId(), item.getPaymentStatus(), item.getPaymentAmount(), item.getPaidAt()))
			.toList();

		return new GetPaymentLogsResponse(
			items,
			query.getPage(),
			query.getSize(),
			query.getTotalElements(),
			query.getTotalPages());
	}

	@Getter
	@AllArgsConstructor
	@Schema(description = "결제 로그 목록 항목")
	static public class Item {
		@Schema(description = "결제 로그 ID", format = "uuid")
		private UUID pamentLogid;
		@Schema(description = "결제 로그 상태")
		private PaymentLogStatus paymentLogStatus;
		@Schema(description = "결제 로그 실패 이유")
		private String paymentLogReason;
		@Schema(description = "결제 ID", format = "uuid")
		private UUID paymentId;
		@Schema(description = "결제 상태")
		private PaymentStatus paymentStatus;
		@Schema(description = "결제 금액")
		private Integer paymentAmount;
		@Schema(description = "결제 일시")
		private LocalDateTime paidAt;
	}
}


