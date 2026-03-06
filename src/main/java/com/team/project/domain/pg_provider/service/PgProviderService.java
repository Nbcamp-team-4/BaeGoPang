package com.team.project.domain.pg_provider.service;

import java.util.UUID;

import com.team.project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com.team.project.domain.pg_provider.api.request.GetPgProvidersRequest;
import com.team.project.domain.pg_provider.api.request.UpdatePgProviderRequest;
import com.team.project.domain.pg_provider.api.response.CreatePgProviderResponse;
import com.team.project.domain.pg_provider.api.response.GetPgProviderResponse;
import com.team.project.domain.pg_provider.api.response.GetPgProvidersResponse;
import com.team.project.domain.pg_provider.api.response.UpdatePgProviderResponse;
import com.team.project.domain.pg_provider.entity.PgProvider;

public interface PgProviderService {
	CreatePgProviderResponse createPgProvider(CreatePgProviderRequest request);

	GetPgProviderResponse getPgProvider(UUID providerId);

	void deletePgProvider(UUID providerId);

	UpdatePgProviderResponse updatePgProvider(UUID providerId, UpdatePgProviderRequest request);

	GetPgProvidersResponse getPgProviders(GetPgProvidersRequest request);

	PgProvider getPgProviderInnerWithException(UUID pgProviderId);
}
