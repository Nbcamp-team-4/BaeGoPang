package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.repository.RegionRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.domain.store.util.GeometryUtil;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final RegionRepository regionRepository;
	private final ProductRepository productRepository;

	// === [점주] 가게 입점 신청 ===
	@Override
	@Transactional
	public StoreResult createStore(CreateStoreCommand command) {
		// 1. 좌표 생성 및 SRID 4326 설정 (명세서 기준)
		Point location = GeometryUtil.createPoint(command.getLongitude(), command.getLatitude());
		location.setSRID(4326);

		// 2. 유저(점주) 존재 확인
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		// 3. 지역 매핑 (명세서: region_id는 NN이므로 반드시 존재해야 함)
		Region region = regionRepository.findActiveRegionContaining(command.getLongitude(), command.getLatitude())
			.orElseThrow(() -> new IllegalArgumentException("AREA_NOT_SUPPORTED"));

		//4. 가게 생성 (빌더 대신 정적 메서드 호출)
		Store store = Store.create(command, user, region, location);

		return StoreResult.from(storeRepository.save(store));
	}

	// === [점주] 가게 정보 수정 ===
	@Override
	@Transactional
	public StoreResult updateStoreByOwner(UUID storeId, UUID userId, UpdateOwnerFieldsCommand command) {
		Store store = findStoreById(storeId);

		// 권한 체크: 명세서의 user_id와 요청한 userId 비교
		if (!store.getUser().getId().equals(userId)) {
			throw new RuntimeException("해당 가게에 대한 수정 권한이 없습니다.");
		}

		store.updateByOwner(command);
		return StoreResult.from(store);
	}

	// === [관리자] 가게 정보 수정 (전권) ===
	@Override
	@Transactional
	public StoreResult updateStoreByAdmin(UpdateStoreByAdminCommand command) {
		Store store = findStoreById(command.getStoreId());

		Region region = regionRepository.findById(command.getRegionId())
			.orElseThrow(() -> new IllegalArgumentException("REGION_NOT_FOUND"));

		store.updateByAdmin(command, region);
		return StoreResult.from(store);
	}

	// === [관리자] 가게 승인 ===
	@Override
	@Transactional
	public StoreResult approveStore(UUID storeId) {
		Store store = findStoreById(storeId);
		store.approve(); // INACTIVE -> OPEN
		return StoreResult.from(store);
	}

	// === [점주] 내 가게 목록 조회 ===
	@Override
	public List<StoreResult> getMyStores(UUID userId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		// Impl에서 구현한 fetch join 메서드 호출 (성능 최적화)
		return storeRepository.findAllByUserIdWithDetails(userId).stream()
			.map(StoreResult::from)
			.toList();
	}

	// === [관리자] 승인 대기 목록 조회 ===
	@Override
	public List<StoreResult> getPendingStores() {
		// 명세서 상 INACTIVE인 것들이 승인 대기 상태
		return storeRepository.findAllByStatus(StoreStatus.INACTIVE).stream()
			.map(StoreResult::from)
			.toList();
	}

	// === [관리자] 전체 가게 목록 조회 ===
	@Override
	public List<StoreResult> getAllStores() {
		// 모든 가게를 조회하되, 삭제되지 않은 것들만 최신순으로 가져옵니다.
		return storeRepository.findAll().stream()
			.filter(store -> store.getDeletedAt() == null) // Soft Delete 체크
			.map(StoreResult::from)
			.toList();
	}

	// === [사용자] 지역별 가게 조회 ===
	@Override
	public List<StoreResult> getStoresByRegion(UUID regionId) {
		// 특정 지역 내 '운영 중(OPEN)'이고 삭제되지 않은 가게만 조회
		// Repository에서 구현한 findAllByRegionIdAndStatus를 활용하세요.
		return storeRepository.findAllByRegionIdAndStatus(regionId, StoreStatus.OPEN).stream()
			.filter(store -> store.getDeletedAt() == null) // Soft Delete 체크
			.map(StoreResult::from)
			.toList();
	}

	// === [공통] 상세 조회 ===
	@Override
	public StoreResult getStoreDetail(UUID storeId) {
		return storeRepository.findDetailById(storeId)
			.map(StoreResult::from)
			.orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));
	}


	// === [삭제] Soft Delete ===
	@Override
	@Transactional
	public void deleteStore(UUID storeId, UUID userId) {
		// 1. 가게 조회 및 권한 체크
		Store store = findStoreById(storeId);
		if (!store.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("ACCESS_DENIED");
		}

		// 2. 가게 Soft Delete
		store.markDeleted(userId);

		// 3. [핵심] 카테고리 매핑 정보 Soft Delete
		// 가게와 연결된 "한식", "치킨" 등의 매핑 데이터를 다 지웁니다.
		//storeCategoryRepository.softDeleteByStoreId(storeId, userId);

		// 4. 연관 데이터(상품, 그룹, 옵션) Soft Delete
		productRepository.softDeleteByStoreId(storeId, userId);
		//productGroupRepository.softDeleteByStoreId(storeId, userId);
		//productOptionRepository.softDeleteByStoreId(storeId, userId);
	}

	// 내부 헬퍼: 삭제되지 않은 가게만 조회
	private Store findStoreById(UUID storeId) {
		return storeRepository.findById(storeId)
			.filter(s -> s.getDeletedAt() == null) // Soft Delete 체크
			.orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));
	}
}
