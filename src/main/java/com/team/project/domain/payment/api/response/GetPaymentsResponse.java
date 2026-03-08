package com.team.project.domain.payment.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;

public class GetPaymentsResponse extends BasePageResponse<GetPaymentsResponse.Item> {

	public GetPaymentsResponse(List<GetPaymentsResponse.Item> content, Integer page, Integer size,
		Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentsResponse from(GetPaymentsQuery query) {
		List<GetPaymentsResponse.Item> items = query.getContent()
			.stream()
			.map(item -> new GetPaymentsResponse.Item(item.getId()))
			.toList();
		return new GetPaymentsResponse(items, query.getPage(), query.getSize(), query.getTotalElements(),
			query.getTotalPages());
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
	}
}
