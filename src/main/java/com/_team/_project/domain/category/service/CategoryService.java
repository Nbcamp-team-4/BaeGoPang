package com._team._project.domain.category.service;

import com._team._project.domain.category.api.request.CreateCategoryRequest;
import com._team._project.domain.category.api.request.UpdateCategoryRequest;
import com._team._project.domain.category.api.response.*;

import java.util.UUID;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryRequest request);

    GetCategoryResponse getCategory(UUID categoryId);

    GetCategoriesResponse getCategories();

    UpdateCategoryResponse updateCategory(UUID categoryId, UpdateCategoryRequest request);

    DeleteCategoryResponse deleteCategory(UUID categoryId); // Soft Delete
}
