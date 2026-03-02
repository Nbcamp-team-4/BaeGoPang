package com._team._project.domain.category.service;

import com._team._project.domain.category.api.request.CreateCategoryRequest;
import com._team._project.domain.category.api.request.UpdateCategoryRequest;
import com._team._project.domain.category.api.response.*;
import com._team._project.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
        return null;
    }

    @Override
    public GetCategoryResponse getCategory(UUID categoryId) {
        return null;
    }

    @Override
    public GetCategoriesResponse getCategories() {
        return null;
    }

    @Override
    public UpdateCategoryResponse updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        return null;
    }

    @Override
    public DeleteCategoryResponse deleteCategory(UUID categoryId) {
        return null;
    }
}
