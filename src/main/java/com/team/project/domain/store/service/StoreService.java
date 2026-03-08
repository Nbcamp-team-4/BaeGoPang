package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.product.entity.Product;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;

public interface StoreService {

	// === [점주] 가게 관리 ===
	StoreResult createStore(CreateStoreCommand command); // 입점 요청
	StoreResult updateStoreByOwner(UUID storeId, UUID userId, UpdateOwnerFieldsCommand command);
	List<StoreResult> getMyStores(UUID userId); // 본인 가게 목록

	//===관리자-사용자 상태변경(영업 시작/종료 또는 승인/차단) ===
	StoreResult updateStatus(UUID storeId, UUID userId, StoreStatus newStatus, String role);

	// === [관리자] 운영 관리 ===
	// 관리자 수정 시에도 userId를 받도록 일관성 유지
	StoreResult updateStoreByAdmin(UUID storeId, UUID userId, UpdateStoreByAdminCommand command); // 전체 정보 수정
	// 통합 필터 조회 (승인 대기/지역별/상태별 전체 포함)
	List<StoreResult> searchStores(SearchStoreCommand command);   // 전체 가게 목록

	// === [사용자/공통] 조회 ===
	StoreResult getStoreDetail(UUID storeId);
	List<Product> getStoreProducts(UUID storeId);// 상세 조회 (메뉴)

	// 좌표 직접 입력 검색 (3km)
	List<StoreResult> searchNearbyStores(Double latitude, Double longitude, UUID categoryId);
	// 유저 주소 기반 검색 (addressId + userId 검증 포함)
	public List<StoreResult> searchByUserIdAddress(UUID addressId, SearchStoreCommand command);

	// === [삭제] Soft Delete ===
	@Transactional
	void deleteStore(UUID storeId, UUID userId);
}

