package com.team.project.domain.payment_log.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetPaymentLogsResponse extends BasePageResponse<GetPaymentLogsResponse.Item> {
	public GetPaymentLogsResponse(List<Item> content, Integer page, Integer size, Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	public static GetPaymentLogsResponse from(GetPaymentLogsQuery query) {
		List<Item> items = query.getContent()
			.stream()
			.map(item -> new Item(item.getId()))
			.toList();

		return new GetPaymentLogsResponse(items, query.getPage(), query.getSize(), query.getTotalElements(),
			query.getTotalPages());
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
	}
}


