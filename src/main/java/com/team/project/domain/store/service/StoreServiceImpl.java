package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.address.entity.UserAddress;
import com.team.project.domain.address.repository.UserAddressRepository;
import com.team.project.domain.category.entity.Category;
import com.team.project.domain.category.repository.CategoryRepository;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.repository.RegionRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.exception.InvalidStoreRequestException;
import com.team.project.domain.store.exception.StoreForbiddenException;
import com.team.project.domain.store.exception.StoreNotFoundException;
import com.team.project.domain.store.exception.StoreNotOperatingException;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.domain.store.util.GeometryUtil;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final RegionRepository regionRepository;
	private final CategoryRepository categoryRepository;
	private final UserAddressRepository userAddressRepository;
	private final ProductRepository productRepository;

	@Override
	public StoreResult createStore(CreateStoreCommand command) {
		Point location = GeometryUtil.createPoint(command.getLongitude(), command.getLatitude());
		location.setSRID(4326);

		User user = userRepository.findById(command.getUserId())
			.orElseThrow(InvalidStoreRequestException::new);

		Region region = regionRepository.findActiveRegionContaining(command.getLongitude(), command.getLatitude())
			.orElseThrow(InvalidStoreRequestException::new);

		//카테고리 검증
		List<Category> categories =
			categoryRepository.findAllByIdInAndDeletedAtIsNull(command.getCategoryIds());

		if (categories.size() != command.getCategoryIds().size()) {
			throw new InvalidStoreRequestException();
		}
		Store store = Store.create(command, user, region, location, categories);

		return StoreResult.from(storeRepository.save(store));
	}

	@Override
	public StoreResult updateStoreByOwner(UUID storeId, UpdateOwnerFieldsCommand command) {
		UUID userId = command.getUserId();

		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (!store.getUser().getId().equals(userId)) {
			throw new StoreForbiddenException();
		}

		store.updateByOwner(command);

		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllById(command.getCategoryIds());
			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}
			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	@Override
	public StoreResult updateStoreByAdmin(UUID storeId, UpdateStoreByAdminCommand command) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		Region region = store.getRegion();
		if (command.getRegionId() != null && !command.getRegionId().equals(region.getId())) {
			region = regionRepository.findById(command.getRegionId())
				.orElseThrow(InvalidStoreRequestException::new);
		}

		store.updateByAdmin(command, region);

		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllById(command.getCategoryIds());
			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}
			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> searchByUserIdAddress(UUID addressId, SearchStoreCommand command) {

		UserAddress address = userAddressRepository.findById(addressId)
			.orElseThrow(InvalidStoreRequestException::new);

		if (!address.getUser().getId().equals(command.getUserId())) {
			throw new StoreForbiddenException();
		}
		log.info("latitude:{}, longitude:{}, categoryId:{}", address.getLatitude(), address.getLongitude(),
			command.getCategoryId());

		Double latitude = address.getLatitude().doubleValue();
		Double longitude = address.getLongitude().doubleValue();

		List<Store> stores = storeRepository.findNearbyStores(
			longitude,
			latitude,
			3000.0,
			command.getCategoryId()
		);

		return stores.stream()
			.map(StoreResult::from)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreResult> searchNearbyStores(Double latitude, Double longitude, UUID categoryId) {
		double distanceMeters = 3000.0;

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

	@Override
	public StoreResult updateStatus(UUID storeId, StoreStatus newStatus, UUID userId, String role) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if ("ROLE_MANAGER".equals(role) || "ROLE_MASTER".equals(role)
			|| "MANAGER".equals(role) || "MASTER".equals(role)) {
			store.updateStatus(newStatus);
		} else if ("ROLE_OWNER".equals(role) || "OWNER".equals(role)) {
			if (!store.getUser().getId().equals(userId)) {
				throw new StoreForbiddenException();
			}

			if (store.getStatus() == StoreStatus.INACTIVE && newStatus == StoreStatus.OPEN) {
				throw new StoreForbiddenException();
			}

			store.updateStatus(newStatus);
		} else {
			throw new StoreForbiddenException();
		}

		return StoreResult.from(store);
	}

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
		return storeRepository.findAllWithFilters(
				command.getStatus(),
				command.getRegionId(),
				command.getUserId()
			).stream()
			.map(StoreResult::from)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public StoreResult getStoreDetail(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

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
	/*@Override
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
	}*/

	// 내부 헬퍼: 삭제되지 않은 가게만 조회
	private Store findStoreById(UUID storeId) {
		return storeRepository.findById(storeId)
			.filter(s -> s.getDeletedAt() == null) // Soft Delete 체크
			.orElseThrow(StoreNotFoundException::new);
	}
}
