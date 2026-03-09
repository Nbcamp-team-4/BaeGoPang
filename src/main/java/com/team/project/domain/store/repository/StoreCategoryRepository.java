package com.team.project.domain.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.store.entity.StoreCategory;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {

	List<StoreCategory> findAllByStore_IdAndDeletedAtIsNull(UUID storeId);
}