package com._team._project.domain.pg_provider.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.pg_provider.entity.PgProvider;

public interface PgProviderJpaRepository extends JpaRepository<PgProvider, Long> {
	
	Optional<PgProvider> findByCode(String code);
}
