package com._team._project.domain.pg_provider.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com._team._project.domain.pg_provider.entity.PgProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PgProviderRepositoryImpl implements PgProviderRepository {

	private final PgProviderJpaRepository pgProviderJpaRepository;

	@Override
	public PgProvider createPgProvider(PgProvider pgProvider) {
		return pgProviderJpaRepository.save(pgProvider);
	}

	@Override
	public Optional<PgProvider> getPgProviderByCode(String code) {
		return pgProviderJpaRepository.findByCode(code);
	}

	@Override
	public Optional<PgProvider> getById(UUID providerId) {
		return pgProviderJpaRepository.findById(providerId);
	}
}
