package com._team._project.domain.pg_provider.api.response;

import java.util.List;
import java.util.UUID;

import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;
import com._team._project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetPgProvidersResponse extends BasePageResponse<GetPgProvidersResponse.Item> {

	@Builder
	public GetPgProvidersResponse(List<GetPgProvidersResponse.Item> content, Integer page, Integer size,
		Long totalElements, Integer totalPages) {
		super(content, page, size, totalElements, totalPages);
	}

	@Data
	@Builder
	static public class Item {
		private UUID id;
		private String code;
		private String name;
		private PgProviderStatus status;
	}
}
