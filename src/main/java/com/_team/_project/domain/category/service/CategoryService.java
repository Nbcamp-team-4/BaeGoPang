package com._team._project.domain.category.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com._team._project.domain.category.api.request.CreateCategoryRequest;
import com._team._project.domain.category.api.request.UpdateCategoryRequest;
import com._team._project.domain.category.api.response.CategoryResponse;
import com._team._project.domain.category.api.response.GetCategoriesResponse;
import com._team._project.domain.category.api.response.GetCategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(UUID userId, CreateCategoryRequest request);
    GetCategoriesResponse getCategoriesForUser(Pageable pageable);
    GetCategoriesResponse getCategoriesForAdmin(Pageable pageable);
    GetCategoryResponse getCategory(UUID categoryId);
    CategoryResponse updateCategory(UUID userId, UUID categoryId, UpdateCategoryRequest request);
    void deleteCategory(UUID userId, UUID categoryId);
}