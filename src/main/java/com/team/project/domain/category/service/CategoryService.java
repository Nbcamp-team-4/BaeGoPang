package com.team.project.domain.category.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.category.api.request.CategoryPageRequest;
import com.team.project.domain.category.api.request.CreateCategoryRequest;
import com.team.project.domain.category.api.request.UpdateCategoryRequest;
import com.team.project.domain.category.api.response.AdminCategoryResponse;
import com.team.project.domain.category.api.response.CategoryResponse;
import com.team.project.domain.category.api.response.GetCategoryResponse;
import com.team.project.global.common.dto.BasePageResponse;

public interface CategoryService {

    CategoryResponse createCategory(UserDto userDto, CreateCategoryRequest request);

    GetCategoryResponse getCategory(UUID categoryId);

    BasePageResponse<CategoryResponse> getCategories(CategoryPageRequest request);

    BasePageResponse<AdminCategoryResponse> getCategoriesForAdmin(CategoryPageRequest request);

    CategoryResponse updateCategory(UserDto userDto, UUID categoryId, UpdateCategoryRequest request);

    void deleteCategory(UserDto userDto, UUID categoryId);
}