package com.team.project.domain.product.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.product.api.request.CreateProductRequest;
import com.team.project.domain.product.api.request.UpdateProductRequest;
import com.team.project.domain.product.api.response.GetProductsResponse;
import com.team.project.domain.product.api.response.ProductResponse;
import com.team.project.domain.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
        @PathVariable UUID productId,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(
        @PathVariable UUID productId,
        @RequestParam UUID userId
    ) {
        productService.deleteProduct(productId, userId);
    }

    @GetMapping
    public GetProductsResponse getProducts(@RequestParam UUID storeId) {
        return productService.getProducts(storeId);
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @PatchMapping("/{productId}/sold-out")
    public ProductResponse markSoldOut(
        @PathVariable UUID productId,
        @RequestParam UUID userId
    ) {
        return productService.markSoldOut(productId, userId);
    }

    @PatchMapping("/{productId}/available")
    public ProductResponse markAvailable(
        @PathVariable UUID productId,
        @RequestParam UUID userId
    ) {
        return productService.markAvailable(productId, userId);
    }

    @PatchMapping("/{productId}/hide")
    public ProductResponse hideProduct(
        @PathVariable UUID productId,
        @RequestParam UUID userId
    ) {
        return productService.hideProduct(productId, userId);
    }

    @PatchMapping("/{productId}/unhide")
    public ProductResponse unhideProduct(
        @PathVariable UUID productId,
        @RequestParam UUID userId
    ) {
        return productService.unhideProduct(productId, userId);
    }
}