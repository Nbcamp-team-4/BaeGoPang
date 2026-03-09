package com.team.project.domain.payment_log.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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
			.map(item -> Item.builder()
				.id(item.getId()).
				build())
			.toList();

		return new GetPaymentLogsResponse(
			items,
			query.getPage(),
			query.getSize(),
			query.getTotalElements(),
			query.getTotalPages());
	}

	@Getter
	@Builder
	@Schema(description = "결제 로그 목록 항목")
	static public class Item {
		@Schema(description = "결제 로그 ID", format = "uuid")
		private UUID id;
	}
}


