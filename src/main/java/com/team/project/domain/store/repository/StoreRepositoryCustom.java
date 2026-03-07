package com.team.project.domain.store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

public interface StoreRepositoryCustom {

	// 1. 상세 조회 (Fetch Join)
	Optional<Store> findDetailById(UUID storeId);

	// 2. [점주] 내 가게 목록 조회
	List<Store> findAllByUserIdWithDetails(UUID userId);

	// 3. [관리자] 통합 필터 조회 (상태, 지역, 유저별)
	List<Store> findAllWithFilters(StoreStatus status, UUID regionId, UUID userId);

	// 4. [사용자] 내 위치 기반 3km 검색 (Native Query)
	List<Store> findNearbyStores(double longitude, double latitude, double distanceInMeters, UUID categoryId);
}