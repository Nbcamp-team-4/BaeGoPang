package com._team._project.domain.region.repository;

import com._team._project.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {

    Optional<Region> getByName(String name);

    boolean existsByName(String name);
}