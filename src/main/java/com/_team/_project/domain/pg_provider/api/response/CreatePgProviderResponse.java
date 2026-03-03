package com._team._project.domain.pg_provider.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePgProviderResponse {

	private UUID id;
	private String code;
	private String name;
	private PgProviderStatus status;
	private LocalDateTime createdAt;
	private UUID createdBy;

	@Builder
	public CreatePgProviderResponse(UUID id, String code, String name, PgProviderStatus status, LocalDateTime createdAt,
		UUID createdBy) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}
}
