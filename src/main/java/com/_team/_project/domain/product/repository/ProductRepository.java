package com._team._project.domain.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}