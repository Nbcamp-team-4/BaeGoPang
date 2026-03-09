package com.team.project.domain.payment.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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
			.map(item -> Item.builder()
				.id(item.getId())
				.build())
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
	@Builder
	@Schema(description = "결제 목록 항목")
	public static class Item {
		@Schema(description = "결제 ID", format = "uuid")
		private UUID id;
	}
}