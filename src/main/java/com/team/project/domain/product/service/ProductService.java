package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.ProductResult;

public interface ProductService {

    // ===== 기본 CRUD =====

    ProductResult createProduct(CreateProductCommand command);

    ProductResult updateProduct(UpdateProductCommand command);
    void deleteProduct(UUID productId, UUID userId);

    List<ProductResult> getProducts(UUID storeId);

    ProductResult getProduct(UUID productId);


    // ===== 상태 변경 =====

    // 품절 처리
    ProductResult markSoldOut(UUID productId, UUID userId);
    // 품절 해제
    ProductResult markAvailable(UUID productId, UUID userId);
    // 숨김 처리
    ProductResult hideProduct(UUID productId, UUID userId);
    // 숨김 해제
    ProductResult unhideProduct(UUID productId, UUID userId);

    // ===== 2차 확장 예정 =====

    // Search(Page/Slice) + 정렬 + size 제한 필요함 (2차)

}