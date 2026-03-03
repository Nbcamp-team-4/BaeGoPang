package com._team._project.domain.pg_provider.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

public interface PgProviderJpaRepository extends JpaRepository<PgProvider, UUID> {

	Optional<PgProvider> findByCode(String code);

	Page<PgProvider> findByCodeContainingIgnoreCase(String code, Pageable pageable);

	Page<PgProvider> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<PgProvider> findByStatus(PgProviderStatus status, Pageable pageable);

	Page<PgProvider> findByCodeContainingIgnoreCaseAndStatus(
		String code,
		PgProviderStatus status,
		Pageable pageable
	);

	Page<PgProvider> findByNameContainingIgnoreCaseAndStatus(
		String name,
		PgProviderStatus status,
		Pageable pageable
	);

	Page<PgProvider> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(String code, String name,
		Pageable pageable);

	Page<PgProvider> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCaseAndStatus(String code, String name,
		PgProviderStatus status, Pageable pageable);

}
