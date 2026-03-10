package com.team.project.domain.store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {

    // 로그인한 OWNER가 실제 해당 가게 주인인지 확인할 때 사용
    Optional<Store> findByIdAndDeletedAtIsNull(UUID storeId);

    Optional<Store> findByIdAndUserId(UUID storeId, UUID userId);
}

