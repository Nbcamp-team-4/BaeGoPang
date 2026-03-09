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
import com.team.project.domain.store.api.request.GetStoresRequest;
import com.team.project.domain.store.api.request.UpdateOwnerFieldsRequest;
import com.team.project.domain.store.api.response.GetStoresResponse;
import com.team.project.domain.store.api.response.StoreProductResponse;
import com.team.project.domain.store.api.response.StoreResponse;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.StoreService;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.domain.store.service.result.StoreResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "가게 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

	private final StoreService storeService;

	@Operation(summary = "가게 등록", description = "가게를 등록합니다.")
	@PostMapping
	@PreAuthorize("hasRole('OWNER')")
	public ResponseEntity<StoreResponse> createStore(@RequestBody @Valid CreateStoreRequest request) {
		// TODO: @AuthenticationPrincipal 로부터 유저 정보 받아오도록 수정 예정
		StoreResult result = storeService.createStore(request.toCommand());
		return ResponseEntity.status(HttpStatus.CREATED).body(StoreResponse.from(result));
	}

	// === [공통] 가게 단건 상세 조회 ===
	@Operation(summary = "가게 상세 조회", description = "가게 상세 정보를 조회합니다.")
	@GetMapping("/{storeId}")
	public ResponseEntity<StoreResponse> getStoreDetail(@PathVariable UUID storeId) {

		StoreResult result = storeService.getStoreDetail(storeId);

		List<StoreProductResponse> products = storeService.getStoreProducts(storeId).stream()
			.map(StoreProductResponse::from)
			.toList();

		return ResponseEntity.ok(StoreResponse.from(result, products));
	}

	// === [MANAGER/MASTER] 가게 전체 조회 ===
	@Operation(summary = "가게 전체 조회", description = "가게 목록을 조회합니다. 이름, 상태, 지역, 카테고리 조건으로 필터링할 수 있으며 페이징이 적용됩니다.")
	@GetMapping
	@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')") // 관리자 권한 유지!
	public ResponseEntity<GetStoresResponse> getStores(
		@RequestParam UUID userId,
		@ModelAttribute GetStoresRequest request
	) {
		SearchStoreCommand command = request.toCommand(userId);
		List<StoreResult> results = storeService.searchStores(command);
		return ResponseEntity.ok(GetStoresResponse.of(results, request));
	}

	// === [OWNER] 내 가게 목록 조회 ===
	@Operation(summary = "내 가게 조회", description = "점주(OWNER)가 자신의 가게 목록을 조회합니다.")
	@GetMapping("/my")
	@PreAuthorize("hasRole('OWNER')")
	public ResponseEntity<GetStoresResponse> getMyStores(
		@RequestParam UUID userId // TODO: Security 적용 후 제거
	) {
		List<StoreResult> results = storeService.getMyStores(userId);
		return ResponseEntity.ok(GetStoresResponse.of(results));
	}

	// === [OWNER] 가게 정보 수정 ===
	@Operation(summary = "가게 정보 수정(점주)", description = "점주(OWNER)가 자신의 가게 정보를 수정합니다.")
	@PatchMapping("/{storeId}")
	@PreAuthorize("hasRole('OWNER')")
	public ResponseEntity<StoreResponse> updateStoreByOwner(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestBody @Valid UpdateOwnerFieldsRequest request) {

		StoreResult result = storeService.updateStoreByOwner(storeId, userId, request.toCommand(userId));
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [MANAGER/MASTER] 가게 전체 정보 수정 ===
	@Operation(summary = "가게 전체 정보 수정", description = "관리자(MANAGER, MASTER)가 가게 전체 정보를 수정합니다.")
	@PatchMapping("/{storeId}/admin")
	@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')") // 관리자 권한 유지!
	public ResponseEntity<StoreResponse> updateStoreByAdmin(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestBody @Valid AdminUpdateStoreRequest request) {

		StoreResult result = storeService.updateStoreByAdmin(storeId, userId, request.toCommand(storeId));
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [공통/관리자] 가게 상태 변경 ===
	@Operation(summary = "가게 상태 변경", description = "가게 상태를 변경합니다.")
	@PatchMapping("/{storeId}/status")
	@PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')") // 점주(OPEN/CLOSED) 및 관리자 권한 유지!
	public ResponseEntity<StoreResponse> updateStoreStatus(
		@PathVariable UUID storeId,
		@RequestParam UUID userId, // TODO: @AuthenticationPrincipal 적용 예정
		@RequestParam String role, // TODO: User/Role 구조 확정 후 제거
		@RequestParam StoreStatus status) { // ?status=OPEN

		StoreResult result = storeService.updateStatus(storeId, userId, status, role);
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [사용자] 내 주소 기준 주변 가게 조회 ===
	@Operation(summary = "주변 가게 조회", description = "사용자의 주소 기준 주변 가게를 조회합니다.")
	@GetMapping("/nearby")
	public ResponseEntity<GetStoresResponse> searchByMyAddress(
		@RequestParam UUID addressId,
		@RequestParam UUID userId, // TODO: Security 적용 후 제거
		@ModelAttribute GetStoresRequest request
	) {
		SearchStoreCommand command = request.toCommand(userId);
		List<StoreResult> results = storeService.searchByUserIdAddress(addressId, command);
		return ResponseEntity.ok(GetStoresResponse.of(results, request));
	}

	// === [MANAGER/MASTER] 가게 삭제 ===
	@Operation(summary = "가게 삭제", description = "관리자가 가게를 삭제합니다.")
	@DeleteMapping("/{storeId}")
	@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')") // 권한 유지!
	public ResponseEntity<Void> deleteStore(
		@PathVariable UUID storeId,
		@RequestParam UUID userId) { // TODO: @AuthenticationPrincipal 적용 예정
		storeService.deleteStore(storeId, userId);
		return ResponseEntity.noContent().build();
	}
}