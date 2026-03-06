package com.team.project.domain.store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

public interface StoreRepositoryCustom {

	// 상세 조회 (지역 정보까지 한 번에 긁어오기)
	Optional<Store> findDetailById(UUID storeId);

	// [점주] 내 가게 목록 조회 (나중에 카테고리 fetch join을 위해 유지)
	List<Store> findAllByUserIdWithDetails(UUID userId);

	// 3. 관리자용 상태별 조회: 승인 대기 중(INACTIVE/PENDING)인 것들 찾기
	List<Store> findAllByStatus(StoreStatus status);

	// [사용자] 지역별/카테고리별 검색
	List<Store> findByRegionAndCategory(UUID regionId, UUID categoryId);

}