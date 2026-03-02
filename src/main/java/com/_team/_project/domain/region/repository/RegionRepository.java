package com._team._project.domain.region.repository;

import com._team._project.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface RegionRepository extends JpaRepository<Region, UUID> {


    //지역명 단건 (중복 불가)
    Optional<Region> findByName(String name);

     //지역명 중복 여부 확인
    boolean existsByName(String name);

     // 활성화된 지역 전체 조회(사용자용)
    List<Region> findAllByActiveTrue();

    //가게-지역 매핑
    //쿼리 필요
    Optional<Region> findActiveRegionContaining(@Param("point") Point point);
}