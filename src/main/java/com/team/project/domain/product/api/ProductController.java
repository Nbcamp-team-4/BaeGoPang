package com.team.project.domain.product.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.team.project.domain.product.api.response.GetProductResponse;
import com.team.project.domain.product.api.response.ProductResponse;
import com.team.project.domain.product.service.ProductService;
import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.GetProductResult;
import com.team.project.domain.product.service.result.ProductResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
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
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
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
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable UUID productId,
        @RequestParam(required = false) UUID userId
    ) {

        productService.deleteProduct(productId, userId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 목록 조회
     */
    @Operation(summary = "상품 목록 조회", description = "특정 가게의 상품 목록을 조회합니다.")
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
     * 상품 상세 조회 (사용자)
     */
    @Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(
        @PathVariable UUID productId
    ) {

        GetProductResult result = productService.getProduct(productId);

        return ResponseEntity.ok(GetProductResponse.from(result));
    }

    /**
     * 상품 상세 조회 (관리자)
     */
    @Operation(summary = "상품 상세 조회 (관리자)", description = "관리자 또는 점주가 상품 상세 정보를 조회합니다.")
    @GetMapping("/{productId}/admin")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<GetProductResponse> getProductForAdmin(
        @PathVariable UUID productId
    ) {

        GetProductResult result = productService.getProductForAdmin(productId);

        return ResponseEntity.ok(GetProductResponse.from(result));
    }

    /**
     * 상품 품절 상태 변경
     */
    @Operation(summary = "상품 품절 상태 변경", description = "상품의 품절 상태를 변경합니다.")
    @PatchMapping("/{productId}/sold-out")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> updateSoldOutStatus(
        @PathVariable UUID productId,
        @RequestParam boolean soldOut,
        @RequestParam(required = false) UUID userId
    ) {

        ProductResult result = soldOut
            ? productService.markSoldOut(productId, userId)
            : productService.markAvailable(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 숨김 상태 변경
     */
    @Operation(summary = "상품 숨김 상태 변경", description = "상품의 숨김 상태를 변경합니다.")
    @PatchMapping("/{productId}/hidden")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> updateHiddenStatus(
        @PathVariable UUID productId,
        @RequestParam boolean hidden,
        @RequestParam(required = false) UUID userId
    ) {

        ProductResult result = hidden
            ? productService.hideProduct(productId, userId)
            : productService.unhideProduct(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }
}