package com._team._project.domain.product.api;

import com._team._project.domain.product.api.request.CreateProductRequest;
import com._team._project.domain.product.api.request.UpdateProductRequest;
import com._team._project.domain.product.api.response.*;
import com._team._project.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PostMapping
    public CreateProductResponse createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}")
    public GetProductResponse getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public GetProductsResponse getProducts() {
        return productService.getProducts();
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PatchMapping("/{productId}")
    public UpdateProductResponse updateProduct(@PathVariable UUID productId,
                                               @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(productId, request);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @DeleteMapping("/{productId}")
    public DeleteProductResponse deleteProduct(@PathVariable UUID productId) {
        return productService.deleteProduct(productId);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PatchMapping("/{productId}/hide")
    public HideProductResponse hideProduct(@PathVariable UUID productId) {
        return productService.hideProduct(productId);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PatchMapping("/{productId}/unhide")
    public HideProductResponse unhideProduct(@PathVariable UUID productId) {
        return productService.unhideProduct(productId);
    }
}
