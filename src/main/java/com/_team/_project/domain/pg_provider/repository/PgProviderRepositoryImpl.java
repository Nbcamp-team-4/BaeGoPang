package com._team._project.domain.pg_provider.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com._team._project.domain.pg_provider.api.request.GetPgProvidersRequest;
import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

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

	@Override
	public void deletePgProvider(UUID providerId) {
		pgProviderJpaRepository.deleteById(providerId);
	}

	@Override
	public Page<PgProvider> getPgProviders(GetPgProvidersRequest request, Pageable pageable) {
		String code = request.getCode();
		String name = request.getName();
		PgProviderStatus status = request.getStatus();

		// code + name + status
		if (code != null && name != null && status != null) {
			return pgProviderJpaRepository
				.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCaseAndStatus(code, name, status, pageable);
		}

		// code + name
		if (code != null && name != null) {
			return pgProviderJpaRepository
				.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(code, name, pageable);
		}

		// code + status
		if (code != null && status != null) {
			return pgProviderJpaRepository
				.findByCodeContainingIgnoreCaseAndStatus(code, status, pageable);
		}

		// name + status
		if (name != null && status != null) {
			return pgProviderJpaRepository
				.findByNameContainingIgnoreCaseAndStatus(name, status, pageable);
		}

		// code only
		if (code != null) {
			return pgProviderJpaRepository
				.findByCodeContainingIgnoreCase(code, pageable);
		}

		// name only
		if (name != null) {
			return pgProviderJpaRepository
				.findByNameContainingIgnoreCase(name, pageable);
		}

		// status only
		if (status != null) {
			return pgProviderJpaRepository
				.findByStatus(status, pageable);
		}

		// 아무것도 없는 경우
		return pgProviderJpaRepository.findAll(pageable);
	}
}
