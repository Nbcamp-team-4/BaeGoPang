package com.team.project.domain.region.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.project.domain.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, UUID> {

    // 지역명 단건(중복 불가)
    Optional<Region> findByName(String name);

    // 지역명 중복 여부 확인
    boolean existsByName(String name);

    // 좌표(POINT)가 포함되는 활성 지역 조회 (추후 store 연결)
    @Query(value = """
        SELECT *
        FROM p_region r
        WHERE r.is_active = true
          AND ST_Contains(r.geom, ST_SetSRID(ST_MakePoint(?1, ?2), 4326))
        LIMIT 1
        """, nativeQuery = true)
    Optional<Region> findActiveRegionContaining(double longitude, double latitude);

    // 사용자용(활성만) 페이징
    Page<Region> findAllByActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    // 관리자용(전체) 페이징
    Page<Region> findAllByOrderByCreatedAtDesc(Pageable pageable);

}