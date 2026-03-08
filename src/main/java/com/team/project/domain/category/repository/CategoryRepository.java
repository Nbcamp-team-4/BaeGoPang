package com.team.project.domain.category.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findById(UUID id);

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    //관리자
    Page<Category> findAll(Pageable pageable);

    //사용자
    Page<Category> findAllByDeletedAtIsNull(Pageable pageable);

    //추후 가게-카테고리 매핑 시 필요
    //List<StoreCategory> findAllByCategoryIdAndDeletedAtIsNull(UUID categoryId);

}