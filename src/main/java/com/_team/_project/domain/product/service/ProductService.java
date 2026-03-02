package com._team._project.domain.product.service;

import com._team._project.domain.product.api.request.CreateProductRequest;
import com._team._project.domain.product.api.request.UpdateProductRequest;
import com._team._project.domain.product.api.response.*;

import java.util.UUID;

public interface ProductService {

    CreateProductResponse createProduct(CreateProductRequest request);

    GetProductResponse getProduct(UUID productId);

    GetProductsResponse getProducts();

    UpdateProductResponse updateProduct(UUID productId, UpdateProductRequest request);

    DeleteProductResponse deleteProduct(UUID productId); // Soft Delete

    HideProductResponse hideProduct(UUID productId);

    HideProductResponse unhideProduct(UUID productId);

    // Search(Page/Slice) + 정렬 + size 제한 필요함 (2차)
}
