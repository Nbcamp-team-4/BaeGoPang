package com.team.project.domain.store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

public interface StoreRepositoryCustom {

	Optional<Store> findDetailById(UUID storeId);

	List<Store> findByUser_IdAndDeletedAtIsNull(UUID userId);

	Page<Store> findAllWithFilters(
		String keyword,
		StoreStatus status,
		UUID regionId,
		UUID userId,
		UUID categoryId,
		Pageable pageable
	);

	Page<Store> findNearbyStores(
		double longitude,
		double latitude,
		double distanceInMeters,
		UUID categoryId,
		Pageable pageable
	);
}