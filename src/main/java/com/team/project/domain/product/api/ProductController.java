package com.team.project.domain.product.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.product.api.request.CreateProductRequest;
import com.team.project.domain.product.api.request.UpdateProductRequest;
import com.team.project.domain.product.api.response.ProductResponse;
import com.team.project.domain.product.service.ProductService;
import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.ProductResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody CreateProductRequest request
    ) {

        CreateProductCommand command = request.toCommand();
        ProductResult result = productService.createProduct(command);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable UUID productId,
        @RequestBody UpdateProductRequest request
    ) {

        UpdateProductCommand command = request.toCommand(productId);
        ProductResult result = productService.updateProduct(command);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable UUID productId
    ) {

        // TODO: Security 적용 후 로그인 사용자 userId 추출
        UUID userId = null;

        productService.deleteProduct(productId, userId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
        @RequestParam UUID storeId
    ) {

        List<ProductResult> results = productService.getProducts(storeId);

        List<ProductResponse> response = results.stream()
            .map(ProductResponse::from)
            .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
        @PathVariable UUID productId
    ) {

        ProductResult result = productService.getProduct(productId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 품절 처리
     */
    @PatchMapping("/{productId}/sold-out")
    public ResponseEntity<ProductResponse> markSoldOut(
        @PathVariable UUID productId
    ) {

        // TODO: Security 적용 후 userId 추출
        UUID userId = null;

        ProductResult result = productService.markSoldOut(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 품절 해제
     */
    @PatchMapping("/{productId}/available")
    public ResponseEntity<ProductResponse> markAvailable(
        @PathVariable UUID productId
    ) {

        // TODO: Security 적용 후 userId 추출
        UUID userId = null;

        ProductResult result = productService.markAvailable(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 숨김 처리
     */
    @PatchMapping("/{productId}/hide")
    public ResponseEntity<ProductResponse> hideProduct(
        @PathVariable UUID productId
    ) {

        // TODO: Security 적용 후 userId 추출
        UUID userId = null;

        ProductResult result = productService.hideProduct(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 숨김 해제
     */
    @PatchMapping("/{productId}/unhide")
    public ResponseEntity<ProductResponse> unhideProduct(
        @PathVariable UUID productId
    ) {

        // TODO: Security 적용 후 userId 추출
        UUID userId = null;

        ProductResult result = productService.unhideProduct(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }
}