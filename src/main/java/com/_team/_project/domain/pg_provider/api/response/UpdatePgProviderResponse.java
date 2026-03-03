package com._team._project.domain.pg_provider.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdatePgProviderResponse {

	private UUID id;
	private String code;
	private String name;
	private PgProviderStatus status;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	@Builder
	public UpdatePgProviderResponse(UUID id, String code, String name, PgProviderStatus status, LocalDateTime updatedAt,
		UUID updatedBy) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static UpdatePgProviderResponse from(PgProvider provider) {
		return UpdatePgProviderResponse.builder()
			.id(provider.getId())
			.code(provider.getCode())
			.name(provider.getName())
			.status(provider.getStatus())
			.updatedAt(provider.getUpdatedAt())
			.updatedBy(provider.getUpdatedBy())
			.build();
	}
}
