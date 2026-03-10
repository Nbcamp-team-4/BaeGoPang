package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.address.entity.UserAddress;
import com.team.project.domain.address.repository.UserAddressRepository;
import com.team.project.domain.auth.dto.UserDto;
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
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

	private static final double NEARBY_DISTANCE_METERS = 3000.0;

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final RegionRepository regionRepository;
	private final CategoryRepository categoryRepository;
	private final UserAddressRepository userAddressRepository;
	private final ProductRepository productRepository;
	private final ProductOptionRepository productOptionRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final ProductOptionManager productOptionManager;

	@Override
	public StoreResult createStore(CreateStoreCommand command) {
		Point location = GeometryUtil.createPoint(command.getLongitude(), command.getLatitude());
		location.setSRID(4326);

		User user = userRepository.findById(command.getUserId())
			.orElseThrow(InvalidStoreRequestException::new);

		Region region = regionRepository.findActiveRegionContaining(command.getLongitude(), command.getLatitude())
			.orElseThrow(InvalidStoreRequestException::new);

		// 카테고리 검증
		List<Category> categories = categoryRepository.findAllByIdInAndDeletedAtIsNull(command.getCategoryIds());

		if (categories.size() != command.getCategoryIds().size()) {
			throw new InvalidStoreRequestException();
		}

		Store store = Store.create(command, user, region, location, categories);
		return StoreResult.from(storeRepository.save(store));
	}

	@Override
	public StoreResult updateStoreByOwner(UUID storeId, UpdateOwnerFieldsCommand command) {
		UUID userId = command.getUserId();

		Store store = findStoreById(storeId);

		if (!store.getUser().getId().equals(userId)) {
			throw new StoreForbiddenException();
		}

		store.updateByOwner(command);

		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllByIdInAndDeletedAtIsNull(command.getCategoryIds());

			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}

			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	@Override
	public StoreResult updateStoreByAdmin(UUID storeId, UpdateStoreByAdminCommand command) {
		Store store = findStoreById(storeId);

		Region region = store.getRegion();
		if (command.getRegionId() != null && !command.getRegionId().equals(region.getId())) {
			region = regionRepository.findById(command.getRegionId())
				.orElseThrow(InvalidStoreRequestException::new);
		}

		store.updateByAdmin(command, region);

		if (command.getCategoryIds() != null && !command.getCategoryIds().isEmpty()) {
			List<Category> newCategories = categoryRepository.findAllByIdInAndDeletedAtIsNull(command.getCategoryIds());

			if (newCategories.size() != command.getCategoryIds().size()) {
				throw new InvalidStoreRequestException();
			}

			store.updateCategories(newCategories);
		}

		return StoreResult.from(store);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreResult> searchByUserIdAddress(UUID addressId, SearchStoreCommand command) {
		UserAddress address = userAddressRepository.findById(addressId)
			.orElseThrow(InvalidStoreRequestException::new);

		if (!address.getUser().getId().equals(command.getUserId())) {
			throw new StoreForbiddenException();
		}

		Double latitude = address.getLatitude().doubleValue();
		Double longitude = address.getLongitude().doubleValue();

		Page<Store> page = storeRepository.findNearbyStores(
			longitude,
			latitude,
			NEARBY_DISTANCE_METERS,
			command.getCategoryId(),
			command.toPageable()
		);

		return page.map(StoreResult::from);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreResult> searchNearbyStores(Double latitude, Double longitude, SearchStoreCommand command) {
		Page<Store> page = storeRepository.findNearbyStores(
			longitude,
			latitude,
			NEARBY_DISTANCE_METERS,
			command.getCategoryId(),
			command.toPageable()
		);

		return page.map(StoreResult::from);
	}

	@Override
	public StoreResult updateStatus(UUID storeId, StoreStatus newStatus, UUID userId, String role) {
		Store store = findStoreById(storeId);

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
	public Page<StoreResult> searchStores(SearchStoreCommand command) {
		Page<Store> page = storeRepository.findAllWithFilters(
			command.getKeyword(),
			command.getStatus(),
			command.getRegionId(),
			command.getUserId(),
			command.getCategoryId(),
			command.toPageable()
		);

		return page.map(StoreResult::from);
	}

	@Override
	@Transactional(readOnly = true)
	public StoreResult getStoreDetail(UUID storeId) {
		Store store = storeRepository.findDetailById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (store.getStatus() == StoreStatus.CLOSED || store.getDeletedAt() != null) {
			throw new StoreNotOperatingException();
		}

		return StoreResult.from(store);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getStoreProducts(UUID storeId) {
		return productRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);
	}

	@Override
	public void deleteStore(UUID storeId, UserDto userDto) {
		UUID userId = userDto.getId();

		Store store = findStoreById(storeId);

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
			storeCategory.markDeleted(userId);
		}

		store.markDeleted(userId);
	}

	// 내부 헬퍼: 삭제되지 않은 가게만 조회
	private Store findStoreById(UUID storeId) {
		return storeRepository.findById(storeId)
			.filter(store -> store.getDeletedAt() == null)
			.orElseThrow(StoreNotFoundException::new);
	}
}