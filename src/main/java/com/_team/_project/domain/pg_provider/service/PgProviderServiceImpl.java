package com._team._project.domain.pg_provider.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.request.GetPgProvidersRequest;
import com._team._project.domain.pg_provider.api.request.UpdatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;
import com._team._project.domain.pg_provider.api.response.GetPgProviderResponse;
import com._team._project.domain.pg_provider.api.response.GetPgProvidersResponse;
import com._team._project.domain.pg_provider.api.response.UpdatePgProviderResponse;
import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.exception.AlreadyDeactivatedPgProviderException;
import com._team._project.domain.pg_provider.exception.DuplicatePgProviderCodeException;
import com._team._project.domain.pg_provider.exception.PgProviderNotFoundException;
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

	@Override
	public GetPgProviderResponse getPgProvider(UUID providerId) {

		// 1. PG사 기본키로 PG사를 찾는다, 검색 결과가 없다면 예외 반환
		PgProvider provider = getPgProviderInnerWithException(providerId);

		// 2. DTO로 반환한다.
		return GetPgProviderResponse.from(provider);
	}

	@Override
	@Transactional
	public void deletePgProvider(UUID providerId) {

		// 1. PG사 기본키로 PG사를 찾는다, 검색 결과가 없다면 예외 반환
		PgProvider provider = getPgProviderInnerWithException(providerId);

		// 2. 이미 비활성화된 경우, 에러 발생
		if (isAreadyDeactivated(provider)) {
			throw new AlreadyDeactivatedPgProviderException();
		}

		// 3. 삭제 표시한다.
		provider.markDeleted(null); // 수정 필요

	}

	@Override
	@Transactional
	public UpdatePgProviderResponse updatePgProvider(UUID providerId, UpdatePgProviderRequest request) {

		// 1. PG사 기본키로 PG사를 찾는다, 검색 결과가 없다면 예외 반환
		PgProvider provider = getPgProviderInnerWithException(providerId);

		// 2. 이미 비활성화된 경우, 에러 발생
		if (isAreadyDeactivated(provider)) {
			throw new AlreadyDeactivatedPgProviderException();
		}

		// 3. 코드의 중복 여부를 확인한뒤,
		Optional<PgProvider> found = pgProviderRepository.getPgProviderByCode(request.getCode());
		if (found.isPresent() && !provider.equals(found.get())) {
			throw new DuplicatePgProviderCodeException();
		}

		// 4. 변경
		boolean result = provider.update(request);

		// 5. dto로 변환
		return UpdatePgProviderResponse.from(provider);
	}

	@Override
	public GetPgProvidersResponse getPgProviders(GetPgProvidersRequest request) {
		int page = request.getPage() == null ? 0 : request.getPage();
		int size = request.getSize() == null ? 10 : request.getSize();

		Pageable pageable = PageRequest.of(page, size);

		Page<PgProvider> pageResult = pgProviderRepository.getPgProviders(request, pageable);

		List<GetPgProvidersResponse.Item> content = pageResult.getContent().stream()
			.map(p -> GetPgProvidersResponse.Item.builder()
				.id(p.getId())
				.code(p.getCode())
				.name(p.getName())
				.status(p.getStatus())
				.build())
			.toList();

		return GetPgProvidersResponse.builder()
			.content(content)
			.page(pageResult.getNumber())
			.size(pageResult.getSize())
			.totalElements(pageResult.getTotalElements())
			.totalPages(pageResult.getTotalPages())
			.build();
	}

	@Override
	public PgProvider getPgProviderInnerWithException(UUID providerId) {
		return pgProviderRepository.getById(providerId)
			.orElseThrow(PgProviderNotFoundException::new);
	}

	private boolean isAreadyDeactivated(PgProvider provider) {
		return provider.getStatus() == PgProviderStatus.DEACTIVE;
	}

}
