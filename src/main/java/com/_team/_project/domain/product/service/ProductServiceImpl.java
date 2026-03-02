package com._team._project.domain.product.service;

import com._team._project.domain.product.api.request.CreateProductRequest;
import com._team._project.domain.product.api.request.UpdateProductRequest;
import com._team._project.domain.product.api.response.*;
import com._team._project.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        return null;
    }

    @Override
    public GetProductResponse getProduct(UUID productId) {
        return null;
    }

    @Override
    public GetProductsResponse getProducts() {
        return null;
    }

    @Override
    public UpdateProductResponse updateProduct(UUID productId, UpdateProductRequest request) {
        return null;
    }

    @Override
    public DeleteProductResponse deleteProduct(UUID productId) {
        return null;
    }

    @Override
    public HideProductResponse hideProduct(UUID productId) {
        return null;
    }

    @Override
    public HideProductResponse unhideProduct(UUID productId) {
        return null;
    }
}
