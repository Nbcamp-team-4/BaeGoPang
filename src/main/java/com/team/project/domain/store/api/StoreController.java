package com.team.project.domain.store.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.store.api.request.CreateStoreRequest;
import com.team.project.domain.store.api.response.StoreResponse;
import com.team.project.domain.store.service.StoreService;
import com.team.project.domain.store.service.result.StoreResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

	private final StoreService storeService;

	// === [점주] 가게 입점 신청 ===
	@PostMapping
	public ResponseEntity<StoreResponse> createStore(@RequestBody @Valid CreateStoreRequest request) {
		StoreResult result = storeService.createStore(request.toCommand());
		// ApiResponse 없이 직접 StoreResponse를 바디에 담음
		return ResponseEntity.status(HttpStatus.CREATED).body(StoreResponse.from(result));
	}

	// === [점주] 본인 가게 목록 조회 ===
	@GetMapping("/my")
	public ResponseEntity<List<StoreResponse>> getMyStores(@RequestParam UUID userId) {
		List<StoreResult> results = storeService.getMyStores(userId);
		List<StoreResponse> response = results.stream().map(StoreResponse::from).toList();
		return ResponseEntity.ok(response);
	}

	// === [공통] 가게 상세 조회 (메뉴 포함) ===
	@GetMapping("/{storeId}")
	public ResponseEntity<StoreResponse> getStoreDetail(@PathVariable UUID storeId) {
		StoreResult result = storeService.getStoreDetail(storeId);
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [관리자] 가게 승인 ===
	@PatchMapping("/{storeId}/approve")
	public ResponseEntity<StoreResponse> approveStore(@PathVariable UUID storeId) {
		StoreResult result = storeService.approveStore(storeId);
		return ResponseEntity.ok(StoreResponse.from(result));
	}

	// === [점주/관리자] 가게 삭제 (Soft Delete) ===
	@DeleteMapping("/{storeId}")
	public ResponseEntity<Void> deleteStore(
		@PathVariable UUID storeId,
		@RequestParam UUID userId
	) {
		storeService.deleteStore(storeId, userId);
		return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환 (관례적)
	}
}