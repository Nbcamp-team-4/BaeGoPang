package com.team.project.domain.category.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.category.api.request.CategoryPageRequest;
import com.team.project.domain.category.api.request.CreateCategoryRequest;
import com.team.project.domain.category.api.request.UpdateCategoryRequest;
import com.team.project.domain.category.api.response.CategoryResponse;
import com.team.project.domain.category.api.response.GetCategoriesResponse;
import com.team.project.domain.category.api.response.GetCategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(UserDto userDto, CreateCategoryRequest request);
    public GetCategoriesResponse getCategoriesForUser(CategoryPageRequest request);
    GetCategoriesResponse getCategoriesForAdmin(CategoryPageRequest request);
    GetCategoryResponse getCategory(UUID categoryId);
    CategoryResponse updateCategory(UserDto userDto, UUID categoryId, UpdateCategoryRequest request);
    void deleteCategory(UserDto userDto, UUID categoryId);
}