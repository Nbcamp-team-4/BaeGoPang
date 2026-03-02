package com._team._project.domain.store.repository;

import com._team._project.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByName(String name);

    boolean existsByName(String name);

    // 목록/상세 조회 시 deletedAt IS NULL 조건 필요함 (1차에서는 service에서 필터하거나, 2차에 쿼리로 통일)
    // Search(Page/Slice) + 정렬 + size 제한 필요함
    // 지역 포함 검증/3km 반경 검색 필요함
}
