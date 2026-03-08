package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.ProductResult;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    /**
     * 상품 생성
     */
    @Override
    public ProductResult createProduct(CreateProductCommand command) {

        // TODO: Security 적용 후 로그인 사용자 정보에서 userId 추출

        Store store = storeRepository.findById(command.getStoreId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

        // TODO: 가게 소유자(OWNER) 또는 관리자 권한 검증 필요

        Product product = Product.create(
            store,
            command.getName(),
            command.getPrice(),
            command.getDescription(),
            command.getUseAiDescription(),
            command.getImageUrl()
        );

        Product savedProduct = productRepository.save(product);

        // TODO: useAiDescription == true 인 경우 AI 설명 생성 로직 추가 예정

        return ProductResult.from(savedProduct);
    }


    /**
     * 상품 수정
     */
    @Override
    public ProductResult updateProduct(UpdateProductCommand command) {

        // TODO: Security 적용 후 userId 추출

        Product product = productRepository.findByIdAndDeletedAtIsNull(command.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // TODO: 상품 수정 권한 검증 (가게 OWNER / 관리자)

        product.update(
            command.getName(),
            command.getPrice(),
            command.getDescription(),
            command.getUseAiDescription(),
            command.getImageUrl()
        );

        return ProductResult.from(product);
    }


    /**
     * 상품 삭제 (Soft Delete)
     */
    @Override
    public void deleteProduct(UUID productId, UUID userId) {

        // TODO: Security 적용 후 userId 자동 주입

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // TODO: 삭제 권한 검증 (가게 OWNER / 관리자)

        product.delete(userId);
    }


    /**
     * 상품 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> getProducts(UUID storeId) {

        // TODO: 관리자/점주 조회 시 hidden 포함 여부 분기 필요

        return productRepository
            .findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(storeId)
            .stream()
            .map(ProductResult::from)
            .toList();
    }


    /**
     * 상품 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResult getProduct(UUID productId) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // TODO: 상품 상세 조회 시 옵션 그룹 + 옵션 아이템 조회 추가 예정

        return ProductResult.from(product);
    }


    /**
     * 상품 품절 처리
     */
    @Override
    public ProductResult markSoldOut(UUID productId, UUID userId) {

        // TODO: Security 적용 후 userId 자동 주입
        // TODO: OWNER / 관리자 권한 검증 필요

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        product.markSoldOut();

        return ProductResult.from(product);
    }


    /**
     * 상품 품절 해제
     */
    @Override
    public ProductResult markAvailable(UUID productId, UUID userId) {

        // TODO: Security 적용 후 userId 자동 주입
        // TODO: OWNER / 관리자 권한 검증 필요

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        product.markAvailable();

        return ProductResult.from(product);
    }


    /**
     * 상품 숨김 처리
     */
    @Override
    public ProductResult hideProduct(UUID productId, UUID userId) {

        // TODO: Security 적용 후 userId 자동 주입
        // TODO: OWNER / 관리자 권한 검증 필요

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        product.hide();

        return ProductResult.from(product);
    }


    /**
     * 상품 숨김 해제
     */
    @Override
    public ProductResult unhideProduct(UUID productId, UUID userId) {

        // TODO: Security 적용 후 userId 자동 주입
        // TODO: OWNER / 관리자 권한 검증 필요

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        product.unhide();

        return ProductResult.from(product);
    }
}