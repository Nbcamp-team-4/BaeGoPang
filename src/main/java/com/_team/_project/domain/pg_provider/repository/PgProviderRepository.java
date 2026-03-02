package com._team._project.domain.pg_provider.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com._team._project.domain.pg_provider.api.request.GetPgProvidersRequest;
import com._team._project.domain.pg_provider.entity.PgProvider;

public interface PgProviderRepository {
	PgProvider createPgProvider(PgProvider pgProvider);

	Optional<PgProvider> getPgProviderByCode(String code);

	Optional<PgProvider> getById(UUID providerId);

	void deletePgProvider(UUID providerId);

	Page<PgProvider> getPgProviders(GetPgProvidersRequest request, Pageable pageable);
}
