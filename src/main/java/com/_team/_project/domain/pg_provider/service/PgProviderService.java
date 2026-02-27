package com._team._project.domain.pg_provider.service;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;

public interface PgProviderService {
	CreatePgProviderResponse createPgProvider(CreatePgProviderRequest request);
}
