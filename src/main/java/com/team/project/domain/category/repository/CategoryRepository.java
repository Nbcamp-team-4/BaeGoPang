package com.team.project.domain.category.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByIdAndDeletedAtIsNull(UUID categoryId);

    boolean existsByNameAndDeletedAtIsNull(String name);

    Page<Category> findAllByDeletedAtIsNull(Pageable pageable);

    Page<Category> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

}