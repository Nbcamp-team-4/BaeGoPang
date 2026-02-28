package com._team._project.domain.pg_provider.api.response;

import java.time.LocalDateTime;
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
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	@Builder
	public GetPgProviderResponse(UUID id, String code, String name, PgProviderStatus status, LocalDateTime createdAt,
		UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static GetPgProviderResponse from(PgProvider provider) {
		return GetPgProviderResponse.builder()
			.id(provider.getId())
			.code(provider.getCode())
			.name(provider.getName())
			.status(provider.getStatus())
			.createdAt(provider.getCreatedAt())
			.createdBy(provider.getCreatedBy())
			.updatedAt(provider.getUpdatedAt())
			.updatedBy(provider.getUpdatedBy())
			.build();
	}

}
