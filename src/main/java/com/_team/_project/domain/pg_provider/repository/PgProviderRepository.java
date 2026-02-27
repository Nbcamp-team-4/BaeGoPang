package com._team._project.domain.pg_provider.repository;

import java.util.Optional;

import com._team._project.domain.pg_provider.entity.PgProvider;

public interface PgProviderRepository {
	PgProvider createPgProvider(PgProvider pgProvider);

	Optional<PgProvider> getPgProviderByCode(String code);
}
