package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.GetProductResult;
import com.team.project.domain.product.service.result.ProductResult;

public interface ProductService {

    ProductResult createProduct(UUID userId, String role, CreateProductCommand command);

    ProductResult updateProduct(UUID userId, String role, UpdateProductCommand command);

    void deleteProduct(UUID productId, UUID userId, String role);

    List<ProductResult> getProducts(UUID storeId);

    //사용자용상품 상세 조회(상품 기본정보+옵션그룹+옵션아이템)
    GetProductResult getProduct(UUID productId);

    //관리자용(품절, 노출 여부 관계없이 모두 확인)
    GetProductResult getProductForAdmin(UUID productId);

    // ===== 상태 변경 =====
    ProductResult markSoldOut(UUID productId, UUID userId, String role);

    ProductResult markAvailable(UUID productId, UUID userId, String role);

    ProductResult hideProduct(UUID productId, UUID userId, String role);

    ProductResult unhideProduct(UUID productId, UUID userId, String role);
}