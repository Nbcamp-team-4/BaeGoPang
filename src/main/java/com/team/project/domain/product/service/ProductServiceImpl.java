package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.product.api.request.CreateProductRequest;
import com.team.project.domain.product.api.request.UpdateProductRequest;
import com.team.project.domain.product.api.response.GetProductsResponse;
import com.team.project.domain.product.api.response.ProductResponse;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.exception.ProductNotFoundException;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    // ===== 생성 =====
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        // 1. UUID를 이용해 DB에서 Store 엔티티를 조회한다.
        Store store = storeRepository.findById(request.getStoreId())
            .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        Product product = new Product(
            store,
            request.getName(),
            request.getPrice(),
            request.getDescription(),
            request.getUseAiDescription(),
            request.getImageUrl()
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
            request.getImageUrl()
        );

        return toResponse(product);
    }

    // ===== 삭제 (Soft Delete) =====
    @Override
    public void deleteProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.delete(userId);
    }

    // ===== 목록 조회 =====
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

        product.markSoldOut();

        return toResponse(product);
    }

    @Override
    public ProductResponse markAvailable(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.markAvailable();

        return toResponse(product);
    }

    // ===== 숨김 =====
    @Override
    public ProductResponse hideProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.hide();

        return toResponse(product);
    }

    @Override
    public ProductResponse unhideProduct(UUID productId, UUID userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.unhide();

        return toResponse(product);
    }

    // ===== Entity → Response =====
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getStore().getId(),
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