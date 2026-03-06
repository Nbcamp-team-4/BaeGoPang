package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;

public interface StoreService {

	// === [점주] 가게 관리 ===
	StoreResult createStore(CreateStoreCommand command); // 입점 요청

	StoreResult updateStoreByOwner(UUID storeId, UUID userId, UpdateOwnerFieldsCommand command);

	List<StoreResult> getMyStores(UUID userId); // 본인 가게 목록

	// === [관리자] 운영 관리 ===
	StoreResult approveStore(UUID storeId); // 승인(INACTIVE -> OPEN)

	StoreResult updateStoreByAdmin(UpdateStoreByAdminCommand command); // 전체 정보 수정

	List<StoreResult> getPendingStores(); // 승인 대기 목록

	List<StoreResult> getAllStores();     // 전체 가게 목록

	// === [사용자/공통] 조회 ===
	StoreResult getStoreDetail(UUID storeId); // 상세 조회 (메뉴 포함용)

	List<StoreResult> getStoresByRegion(UUID regionId); // 지역별 조회

	// === [삭제] Soft Delete ===
	@Transactional
	void deleteStore(UUID storeId, UUID userId);
}

