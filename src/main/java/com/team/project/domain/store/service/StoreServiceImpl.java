package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.category.entity.Category;
import com.team.project.domain.category.repository.CategoryRepository;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.entity.ProductOption;
import com.team.project.domain.product.repository.ProductOptionRepository;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.product.service.ProductOptionManager;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.repository.RegionRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.entity.StoreCategory;
import com.team.project.domain.store.exception.InvalidStoreRequestException;
import com.team.project.domain.store.exception.StoreForbiddenException;
import com.team.project.domain.store.exception.StoreNotFoundException;
import com.team.project.domain.store.exception.StoreNotOperatingException;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.repository.StoreCategoryRepository;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.domain.store.util.GeometryUtil;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserAddress;
import com.team.project.domain.user.repository.UserAddressRepository;
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
	private final ProductOptionRepository productOptionRepository;
	private final ProductOptionManager productOptionManager;
	private final CategoryRepository categoryRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final UserAddressRepository userAddressRepository;


	// === [점주] 가게 입점 신청 ===
	@Override
	@Transactional
	public StoreResult createStore(CreateStoreCommand command) {
		// 1. 좌표 생성
		Point location = GeometryUtil.createPoint(command.getLongitude(), command.getLatitude());
		location.setSRID(4326);

		// 2. 유저 확인
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(InvalidStoreRequestException::new);

		// 3. 지역 확인
		Region region = regionRepository.findActiveRegionContaining(command.getLongitude(), command.getLatitude())
			.orElseThrow(InvalidStoreRequestException::new);

		// 4. 카테고리 검증 및 조회 (Command에 List<UUID> categoryIds 가 있다고 가정)
		List<Category> categories = categoryRepository.findAllById(command.getCategoryIds());
		if (categories.isEmpty() || categories.size() != command.getCategoryIds().size()) {
			throw new InvalidStoreRequestException();
		}

		// 5. 가게 생성 (정적 팩토리 메서드 호출 한 줄로 끝!)
		Store store = Store.create(command, user, region, location, categories);

		return StoreResult.from(storeRepository.save(store));
	}

	// === [점주] 가게 정보 수정 ===
	@Override
	@Transactional
	public StoreResult updateStoreByOwner(UUID storeId, UUID userId, UpdateOwnerFieldsCommand command) {
		// TODO: SecurityContext (@AuthenticationPrincipal)를 통해 유저 정보 받아오도록 수정 예정

		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 1. 권한 검증
		if (!store.getUser().getId().equals(userId)) {
			throw new StoreForbiddenException();
		}

		// 2. 점주 전용 기본 정보 수정
		store.updateByOwner(command);

		// 3. 카테고리 수정
		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllById(command.getCategoryIds());
			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}
			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	// === [관리자] 가게 정보 수정 ===
	@Override
	@Transactional
	public StoreResult updateStoreByAdmin(UUID storeId, UUID userId, UpdateStoreByAdminCommand command) {
		// TODO: SecurityContext (@AuthenticationPrincipal)를 통해 유저 정보 받아오도록 수정 예정

		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 1. 지역(Region) 변경 여부 확인 및 조회
		Region region = store.getRegion();
		if (command.getRegionId() != null && !command.getRegionId().equals(region.getId())) {
			region = regionRepository.findById(command.getRegionId())
				.orElseThrow(InvalidStoreRequestException::new);
		}

		// 2. 관리자 전용 모든 정보 수정
		store.updateByAdmin(command, region);

		// 3. 카테고리 수정 (선택적)
		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllById(command.getCategoryIds());
			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}
			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	// === [사용자] 내 주소(단일 주소) 기준 주변 3km 가게 조회 ===
	// TODO: Security 적용 후, addressId가 로그인한 사용자의 주소인지 검증 로직 추가
	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> searchByUserIdAddress(UUID addressId, SearchStoreCommand command) {

		// 1. 사용자 주소 조회
		UserAddress address = userAddressRepository.findById(addressId)
			.orElseThrow(InvalidStoreRequestException::new);

		// 2. 주소의 위도/경도를 Double 값으로 변환
		Double latitude = address.getLatitude().doubleValue();
		Double longitude = address.getLongitude().doubleValue();

		// 3. 반경 3km 이내 가게 조회
		// - categoryId가 null이면 전체 조회
		// - categoryId가 있으면 해당 카테고리 가게만 조회
		List<Store> stores = storeRepository.findNearbyStores(
			longitude,
			latitude,
			3000.0,
			command.getCategoryId()
		);

		// 4. Entity -> Result 변환 후 반환
		return stores.stream()
			.map(StoreResult::from)
			.toList();
	}

	// searchByUserIdAddress 내부에서 호출하는 실제 검색 엔진
	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> searchNearbyStores(Double latitude, Double longitude, UUID categoryId) {
		double distanceMeters = 3000.0; // 3km

		// Repository의 @Query(nativeQuery = true) 호출
		// 가이드의 규칙대로 longitude를 첫 번째 인자로 전달!
		List<Store> stores = storeRepository.findNearbyStores(
			longitude,
			latitude,
			distanceMeters,
			categoryId
		);

		return stores.stream()
			.map(StoreResult::from)
			.toList();
	}

	// === [관리자] 가게 승인 ===
	@Transactional
	public StoreResult updateStatus(UUID storeId, UUID userId, StoreStatus newStatus, String role) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 1. 관리자(MANAGER/MASTER)인 경우: 모든 상태로 변경 가능 (승인/차단 등)
		if ("MANAGER".equals(role) || "MASTER".equals(role)) {
			store.updateStatus(newStatus);
		}
		// 2. 점주(OWNER)인 경우: 본인 가게여야 하고, OPEN <-> CLOSED 사이만 가능
		else if ("OWNER".equals(role)) {
			if (!store.getUser().getId().equals(userId)) {
				throw new StoreForbiddenException();
			}

			// 점주는 스스로 '승인(INACTIVE -> OPEN)'을 할 수 없음
			if (store.getStatus() == StoreStatus.INACTIVE && newStatus == StoreStatus.OPEN) {
				throw new StoreForbiddenException();
			}

			store.updateStatus(newStatus);
		}

		return StoreResult.from(store);
	}

	// === [점주] 내 가게 목록 조회 ===
	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> getMyStores(UUID userId) {
		List<Store> stores = storeRepository.findByUser_IdAndDeletedAtIsNull(userId);

		return stores.stream()
			.map(StoreResult::from)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> searchStores(SearchStoreCommand command) {
		// // TODO: SecurityContext (@AuthenticationPrincipal)를 통해 관리자 권한 확인 로직 추가 예정

		return storeRepository.findAllWithFilters(
				command.getStatus(),
				command.getRegionId(),
				command.getUserId()
			).stream()
			.map(StoreResult::from)
			.toList();
	}

	// === [공통] 가게 단건 조회 (메뉴 포함용) ===
	@Override // 이름이 getStore -> getStoreDetail 로 변경됨!
	@Transactional(readOnly = true)
	public StoreResult getStoreDetail(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 상태가 CLOSED이거나 삭제된 가게는 조회 불가 처리
		if (store.getStatus() == StoreStatus.CLOSED || store.getDeletedAt() != null) {
			throw new StoreNotOperatingException();
		}

		return StoreResult.from(store);
	}

	// === 메뉴 메서드 ===
	@Override
	@Transactional(readOnly = true)
	public List<Product> getStoreProducts(UUID storeId) {
		return productRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);
	}

	// === [삭제] Soft Delete ===
	@Override
	@Transactional
	public void deleteStore(UUID storeId, UUID userId) {
		Store store = findStoreById(storeId);

		if (!store.getUser().getId().equals(userId)) {
			throw new StoreForbiddenException();
		}

		List<Product> products = productRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);

		for (Product product : products) {
			List<ProductOption> optionGroups =
				productOptionRepository.findAllByProductIdAndDeletedAtIsNull(product.getId());

			for (ProductOption optionGroup : optionGroups) {
				productOptionManager.deleteOptionGroup(optionGroup, userId);
			}

			product.delete(userId);
		}

		List<StoreCategory> storeCategories =
			storeCategoryRepository.findAllByStore_IdAndDeletedAtIsNull(storeId);

		for (StoreCategory storeCategory : storeCategories) {
			storeCategory.delete(userId);
		}

		store.markDeleted(userId);
	}

	// 내부 헬퍼: 삭제되지 않은 가게만 조회
	private Store findStoreById(UUID storeId) {
		return storeRepository.findById(storeId)
			.filter(s -> s.getDeletedAt() == null) // Soft Delete 체크
			.orElseThrow(StoreNotFoundException::new);
	}
}
