package com._team._project.domain.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._team._project.domain.product.api.request.CreateProductRequest;
import com._team._project.domain.product.api.request.UpdateProductRequest;
import com._team._project.domain.product.api.response.GetProductsResponse;
import com._team._project.domain.product.api.response.ProductResponse;
import com._team._project.domain.product.entity.Product;
import com._team._project.domain.product.exception.ProductNotFoundException;
import com._team._project.domain.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ===== 생성 =====
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        Product product = new Product(
            request.getStoreId(),
            request.getName(),
            request.getPrice(),
            request.getDescription(),
            request.getUseAiDescription(),
            request.getImageUrl(),
            request.getUserId()
        );

        productRepository.save(product);

        return toResponse(product);
    }

    // ===== 수정 =====
    @Override
    public ProductResponse updateProduct(UUID productId, UpdateProductRequest request) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.update(
            request.getName(),
            request.getPrice(),
            request.getDescription(),
            request.getUseAiDescription(),
            request.getImageUrl(),
            request.getUserId()
        );

        return toResponse(product);
    }

    // ===== 삭제 (Soft) =====
    @Override
    public void deleteProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.delete(userId);
    }

    // ===== 목록 조회 (숨김 제외) =====
    @Override
    @Transactional(readOnly = true)
    public GetProductsResponse getProducts(UUID storeId) {

        List<ProductResponse> products =
            productRepository
                .findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(storeId)
                .stream()
                .map(this::toResponse)
                .toList();

        return GetProductsResponse.builder()
            .products(products)
            .build();
    }

    // ===== 단건 조회 =====
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID productId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        if (product.getDeletedAt() != null) {
            throw new ProductNotFoundException();
        }

        return toResponse(product);
    }

    // ===== 품절 =====
    @Override
    public ProductResponse markSoldOut(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.markSoldOut(userId);

        return toResponse(product);
    }

    @Override
    public ProductResponse markAvailable(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.markAvailable(userId);

        return toResponse(product);
    }

    // ===== 숨김 =====
    @Override
    public ProductResponse hideProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.hide(userId);

        return toResponse(product);
    }

    @Override
    public ProductResponse unhideProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.unhide(userId);

        return toResponse(product);
    }

    // ===== 매핑 =====
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getStoreId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getUseAiDescription(),
            product.getImageUrl(),
            product.getIsSoldOut(),
            product.getIsHidden(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}