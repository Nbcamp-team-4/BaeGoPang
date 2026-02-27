package com._team._project.domain.pg_provider.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;
import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.exception.DuplicatePgProviderCodeException;
import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;
import com._team._project.domain.pg_provider.repository.PgProviderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PgProviderServiceImpl implements PgProviderService {

	private final PgProviderRepository pgProviderRepository;

	@Override
	@Transactional
	public CreatePgProviderResponse createPgProvider(CreatePgProviderRequest request) {

		// 1. PG사 코드의 중복 여부를 확인한뒤,
		Optional<PgProvider> found = pgProviderRepository.getPgProviderByCode(request.getCode());
		if (found.isPresent()) {
			throw new DuplicatePgProviderCodeException();
		}

		// 2. 중복되지 않는다면 PG사 엔티티 생성
		PgProvider pgProvider = PgProvider.builder()
			.code(request.getCode())
			.name(request.getName())
			.status(PgProviderStatus.ACTIVE)
			.build();

		// 3. 저장
		PgProvider saved = pgProviderRepository.createPgProvider(pgProvider);

		return CreatePgProviderResponse.builder()
			.id(saved.getId())
			.code(saved.getCode())
			.name(saved.getName())
			.status(saved.getStatus())
			.createdAt(saved.getCreatedAt())
			.createdBy(saved.getCreatedBy())
			.build();
	}

}
