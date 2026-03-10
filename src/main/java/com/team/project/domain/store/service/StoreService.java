package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;

public interface StoreService {

	// === [점주] 가게 관리 ===
	StoreResult createStore(CreateStoreCommand command);
	StoreResult updateStoreByOwner(UUID storeId, UpdateOwnerFieldsCommand command);
	List<StoreResult> getMyStores(UUID userId);

	// === [관리자-사용자 상태변경(영업 시작/종료 또는 승인/차단) ===
	StoreResult updateStatus(UUID storeId, StoreStatus newStatus, UUID userId, String role);

	// === [관리자] 운영 관리 ===
	StoreResult updateStoreByAdmin(UUID storeId, UpdateStoreByAdminCommand command);
	Page<StoreResult> searchStores(SearchStoreCommand command);

	// === [사용자/공통] 조회 ===
	StoreResult getStoreDetail(UUID storeId);
	List<Product> getStoreProducts(UUID storeId);

	// 좌표 직접 입력 검색 (3km)
	Page<StoreResult> searchNearbyStores(Double latitude, Double longitude, SearchStoreCommand command);

	// 유저 주소 기반 검색 (addressId + userId 검증 포함)
	Page<StoreResult> searchByUserIdAddress(UUID addressId, SearchStoreCommand command);

	// === [삭제] Soft Delete ===
	void deleteStore(UUID storeId, UserDto userDto);
}

