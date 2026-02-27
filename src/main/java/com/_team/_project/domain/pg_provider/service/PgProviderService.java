package com._team._project.domain.pg_provider.service;

import java.util.UUID;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;
import com._team._project.domain.pg_provider.api.response.GetPgProviderResponse;

public interface PgProviderService {
	CreatePgProviderResponse createPgProvider(CreatePgProviderRequest request);

	GetPgProviderResponse getPgProvider(UUID providerId);

	void deletePgProvider(UUID providerId);
}
