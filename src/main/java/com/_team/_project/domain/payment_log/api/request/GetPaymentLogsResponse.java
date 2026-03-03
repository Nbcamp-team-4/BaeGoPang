package com._team._project.domain.payment_log.api.request;

import java.util.List;
import java.util.UUID;

import com._team._project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetPaymentLogsResponse extends BasePageResponse<GetPaymentLogsResponse.Item> {

	@Builder
	public GetPaymentLogsResponse(List<Item> content, Integer page, Integer size, Long totalElements,
		Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
	}
}


