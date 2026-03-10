package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "결제 목록 조회 응답")
@Getter
public class GetPaymentsResponse extends BasePageResponse<GetPaymentsResponse.Item> {

	public GetPaymentsResponse(
		List<Item> content,
		Integer page,
		Integer size,
		Long totalElements,
		Integer totalPages
	) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentsResponse from(GetPaymentsQuery query) {

		List<Item> items = query.getContent()
			.stream()
			.map(item -> new Item(item.getId(), item.getStatus(), item.getAmount(), item.getPaidAt()))
			.toList();

		return new GetPaymentsResponse(
			items,
			query.getPage(),
			query.getSize(),
			query.getTotalElements(),
			query.getTotalPages()
		);
	}

	@Getter
	@AllArgsConstructor
	@Schema(description = "결제 목록 항목")
	public static class Item {
		@Schema(description = "결제 ID", format = "uuid")
		private UUID id;
		@Schema(description = "결제 상태")
		private PaymentStatus status;
		@Schema(description = "결제 가격")
		private Integer amount;
		@Schema(description = "결제 일시")
		private LocalDateTime paidAt;
	}
}