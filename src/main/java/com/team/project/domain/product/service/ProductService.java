package com.team.project.domain.product.service;

import java.util.UUID;

import com.team.project.domain.product.api.request.CreateProductRequest;
import com.team.project.domain.product.api.request.UpdateProductRequest;
import com.team.project.domain.product.api.response.GetProductsResponse;
import com.team.project.domain.product.api.response.ProductResponse;

public interface ProductService {

    // ===== 기본 CRUD =====

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(UUID productId, UpdateProductRequest request);

    void deleteProduct(UUID productId, UUID userId);

    GetProductsResponse getProducts(UUID storeId);

    ProductResponse getProduct(UUID productId);


    // ===== 상태 변경 =====

    // 품절 처리
    ProductResponse markSoldOut(UUID productId, UUID userId);

    // 품절 해제
    ProductResponse markAvailable(UUID productId, UUID userId);

    // 숨김 처리
    ProductResponse hideProduct(UUID productId, UUID userId);

    // 숨김 해제
    ProductResponse unhideProduct(UUID productId, UUID userId);


    // ===== 2차 확장 예정 =====

    // Search(Page/Slice) + 정렬 + size 제한 필요함 (2차)

}