package com.team.project.domain.store.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.store.api.request.AdminUpdateStoreRequest;
import com.team.project.domain.store.api.request.CreateStoreRequest;
import com.team.project.domain.store.api.request.UpdateOwnerFieldsRequest;
import com.team.project.domain.store.api.response.GetStoresResponse;
import com.team.project.domain.store.api.response.StoreResponse;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.StoreService;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.global.common.dto.BasePageRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

	private final StoreService storeService;

	// === [OWNER] 가게 입점 신청 ===
	@PostMapping
	@PreAuthorize("hasRole('OWNER')")
	public ResponseEntity<StoreResponse> createStore(@RequestBody @Valid CreateStoreRequest request) {
		// TODO: @AuthenticationPrincipal 로부터 유저 정보 받아오도록 수정 예정
		StoreResult result = storeService.createStore(request.toCommand());
		return ResponseEntity.status(HttpStatus.CREATED).body(StoreResponse.from(result));
	}

	// === [공통] 가게 단건 상세 조회 ===
	@GetMapping("/{storeId}")
	public ResponseEntity<StoreResponse> getStoreDetail(@PathVariable UUID storeId) {
		StoreResult result = storeService.getStoreDetail(storeId);
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [OWNER] 가게 정보 수정 ===
	// URL: PATCH /api/stores/{storeId}?userId=...
	@PatchMapping("/{storeId}")
	@PreAuthorize("hasRole('OWNER')") // 점주 권한 유지!
	public ResponseEntity<StoreResponse> updateStoreByOwner(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestBody @Valid UpdateOwnerFieldsRequest request) {

		StoreResult result = storeService.updateStoreByOwner(storeId, userId, request.toCommand(userId));
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [MANAGER/MASTER] 가게 전체 정보 수정 ===
	// URL: PATCH /api/stores/{storeId}/admin?userId=...
	@PatchMapping("/{storeId}/admin")
	//@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')") // 관리자 권한 유지!
	public ResponseEntity<StoreResponse> updateStoreByAdmin(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestBody @Valid AdminUpdateStoreRequest request) {

		StoreResult result = storeService.updateStoreByAdmin(storeId, userId, request.toCommand(storeId));
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [공통/관리자] 가게 상태 변경 ===
	// URL: PATCH /api/stores/{storeId}/status?status=OPEN&userId=...&role=...
	@PatchMapping("/{storeId}/status")
	//@PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')") // 점주(OPEN/CLOSED) 및 관리자 권한 유지!
	public ResponseEntity<StoreResponse> updateStoreStatus(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestParam String role,
		@RequestParam StoreStatus status) { // ?status=OPEN

		StoreResult result = storeService.updateStatus(storeId, userId, status, role);
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [사용자] 내 주소 기반 주변 가게 조회 ===
	// GET /api/stores/search/address?addressId=...&categoryId=...
	@GetMapping("/search/address")
	public ResponseEntity<GetStoresResponse> searchByMyAddress(
		@RequestParam UUID addressId,
		@RequestParam UUID userId, // TODO: SecurityContext 적용 후 제거 대상
		@RequestParam(required = false) UUID categoryId,
		@ModelAttribute BasePageRequest pageRequest // 페이징 요청 객체
		) {
		// 서비스의 파라미터 순서에 맞춰 전달
		List<StoreResult> results = storeService.searchByUserIdAddress(addressId, userId, categoryId);
		return ResponseEntity.ok(GetStoresResponse.of(results, pageRequest));
	}

	// === [MANAGER/MASTER/OWNER] 가게 삭제 ===
	@DeleteMapping("/{storeId}")
	//@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')") // 권한 유지!
	public ResponseEntity<Void> deleteStore(
		@PathVariable UUID storeId,
		@RequestParam UUID userId) { // TODO: @AuthenticationPrincipal 적용 예정
		storeService.deleteStore(storeId, userId);
		return ResponseEntity.noContent().build();
	}
}