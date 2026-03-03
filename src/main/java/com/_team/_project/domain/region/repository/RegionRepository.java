package com._team._project.domain.region.repository;

import com._team._project.domain.region.entity.Region;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {

    // 지역명 단건(중복 불가)
    Optional<Region> findByName(String name);

    // 지역명 중복 여부 확인
    boolean existsByName(String name);

    // 활성화된 지역 전체 조회(사용자용)
    List<Region> findAllByActiveTrue();

    // 좌표(POINT)가 포함되는 활성 지역 조회
    // PostGIS: ST_Contains(geom, point)
    @Query(value = "select * from p_region r where r.is_active = true and ST_Contains(r.geom,point)", nativeQuery = true)
    Optional<Region> findActiveRegionContaining(@Param("point") Point point);
}
