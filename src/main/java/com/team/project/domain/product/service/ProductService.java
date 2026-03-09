package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.GetProductResult;
import com.team.project.domain.product.service.result.ProductResult;

public interface ProductService {

    // ===== 기본 CRUD =====

    //상품 생성
    ProductResult createProduct(CreateProductCommand command);
    //상품수정
    ProductResult updateProduct(UpdateProductCommand command);
    //상품삭제
    void deleteProduct(UUID productId, UUID userId);
    //상품 목록조회(가게 상세메뉴)
    List<ProductResult> getProducts(UUID storeId);
    //상품 상세 조회(상품 기본정보+옵션그룹+옵션아이템)
    //사용자용
    GetProductResult getProduct(UUID productId);
    //관리자용(품절, 노출 여부 관계없이 모두 확인)
    GetProductResult getProductForAdmin(UUID productId);


    // ===== 상태 변경 =====
    // 품절 처리
    ProductResult markSoldOut(UUID productId, UUID userId);
    // 품절 해제
    ProductResult markAvailable(UUID productId, UUID userId);
    // 숨김 처리
    ProductResult hideProduct(UUID productId, UUID userId);
    // 숨김 해제
    ProductResult unhideProduct(UUID productId, UUID userId);

}