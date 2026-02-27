package com._team._project.domain.pg_provider.api.response;

import java.util.UUID;

import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPgProviderResponse {

	private UUID id;
	private String code;
	private String name;
	private PgProviderStatus status;

	@Builder
	public GetPgProviderResponse(UUID id, String code, String name, PgProviderStatus status) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
	}

	public static GetPgProviderResponse from(PgProvider provider) {
		return GetPgProviderResponse.builder()
			.id(provider.getId())
			.code(provider.getCode())
			.name(provider.getName())
			.status(provider.getStatus())
			.build();
	}
}
