package com._team._project.domain.product.repository;

import com._team._project.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // 목록/상세 조회 시 deletedAt IS NULL 조건 필요함
    // Search(Page/Slice) + 정렬 + size 제한 필요함
    // Store/Category 조건 검색 필요함
}
