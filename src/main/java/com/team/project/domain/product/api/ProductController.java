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
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody CreateProductRequest request
    ) {
        // TODO: Security 적용 후 @AuthenticationPrincipal 등으로 로그인 사용자 정보 주입

        CreateProductCommand command = request.toCommand();
        ProductResult result = productService.createProduct(command);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable UUID productId,
        @RequestBody UpdateProductRequest request
    ) {
        // TODO: Security 적용 후 로그인 사용자 정보 기반 권한 검증
        // TODO: 현재는 테스트를 위해 productId만 path 로 받고 request 에서 command 변환

        UpdateProductCommand command = request.toCommand(productId);
        ProductResult result = productService.updateProduct(command);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable UUID productId,
        @RequestParam(required = false) UUID userId
    ) {
        // TODO: Security 적용 후 userId 파라미터 제거

        productService.deleteProduct(productId, userId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 목록 조회
     * 사용자 화면용: 노출 상품 + 품절 제외
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
     * 사용자 화면용: 숨김/품절 상품 제외
     */
    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(
        @PathVariable UUID productId
    ) {
        GetProductResult result = productService.getProduct(productId);

        return ResponseEntity.ok(GetProductResponse.from(result));
    }

    /**
     * 상품 상세 조회
     * 관리자/점주 화면용: 숨김/품절 여부 관계없이 조회
     */

    @GetMapping("/{productId}/admin")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<GetProductResponse> getProductForAdmin(
        @PathVariable UUID productId
    ) {
        // TODO: Security 적용 후 OWNER / MANAGER / MASTER 권한 검증 추가

        GetProductResult result = productService.getProductForAdmin(productId);

        return ResponseEntity.ok(GetProductResponse.from(result));
    }

    /**
     * 상품 품절 여부 변경
     * soldOut=true  -> 품절 처리
     * soldOut=false -> 품절 해제
     */

    @PatchMapping("/{productId}/sold-out")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> updateSoldOutStatus(
        @PathVariable UUID productId,
        @RequestParam boolean soldOut,
        @RequestParam(required = false) UUID userId
    ) {
        // TODO: Security 적용 후 userId 파라미터 제거
        // TODO: 현재는 테스트를 위해 userId 를 request param 으로 받음

        ProductResult result = soldOut
            ? productService.markSoldOut(productId, userId)
            : productService.markAvailable(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }

    /**
     * 상품 숨김 여부 변경
     * hidden=true  -> 숨김 처리
     * hidden=false -> 숨김 해제
     */
    @PatchMapping("/{productId}/hidden")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ProductResponse> updateHiddenStatus(
        @PathVariable UUID productId,
        @RequestParam boolean hidden,
        @RequestParam(required = false) UUID userId
    ) {
        // TODO: Security 적용 후 userId 파라미터 제거
        // TODO: 현재는 테스트를 위해 userId 를 request param 으로 받음

        ProductResult result = hidden
            ? productService.hideProduct(productId, userId)
            : productService.unhideProduct(productId, userId);

        return ResponseEntity.ok(ProductResponse.from(result));
    }
}