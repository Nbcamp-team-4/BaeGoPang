package com._team._project.domain.category.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._team._project.domain.category.api.request.CreateCategoryRequest;
import com._team._project.domain.category.api.request.UpdateCategoryRequest;
import com._team._project.domain.category.api.response.CategoryResponse;
import com._team._project.domain.category.api.response.GetCategoriesResponse;
import com._team._project.domain.category.api.response.GetCategoryResponse;
import com._team._project.domain.category.entity.Category;
import com._team._project.domain.category.exception.CategoryDuplicateException;
import com._team._project.domain.category.exception.CategoryNotFoundException;
import com._team._project.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(UUID userId, CreateCategoryRequest request) {
        // 삭제 안 된 카테고리 기준으로 중복 체크가 더 안전함
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryDuplicateException();
        }

        Category category = Category.builder()
            .name(request.getName())
            .createdBy(userId)
            .build();

        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCategoryResponse getCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        return GetCategoryResponse.of(CategoryResponse.from(category));
    }

    /**
     * 사용자용(삭제 제외) 페이징 조회
     */
    @Override
    @Transactional(readOnly = true)
    public GetCategoriesResponse getCategoriesForUser(Pageable pageable) {
        Page<Category> page = categoryRepository.findAllByDeletedAtIsNull(pageable);

        return new GetCategoriesResponse(
            page.getContent().stream().map(CategoryResponse::from).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    /**
     * 관리자용(전체) 페이징 조회
     */
    @Override
    @Transactional(readOnly = true)
    public GetCategoriesResponse getCategoriesForAdmin(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);

        return new GetCategoriesResponse(
            page.getContent().stream().map(CategoryResponse::from).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    public CategoryResponse updateCategory(UUID userId, UUID categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        boolean nameChanged = !category.getName().equals(request.getName());
        if (nameChanged && categoryRepository.existsByName(request.getName())) {
            throw new CategoryDuplicateException();
        }

        category.update(request.getName(), userId);
        return CategoryResponse.from(category);
    }

    @Override
    public void deleteCategory(UUID userId, UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        //추후 매핑 연결 시 가게-카테고리 매핑 삭제
        /*List<StoreCategory> mappings =
            storeCategoryRepository.findAllByCategoryIdAndDeletedAtIsNull(categoryId);

        for (StoreCategory mapping : mappings) {
            mapping.softDelete(userId);
        }
        */
        // 2카테고리 soft delete
        category.softDelete(userId);
    }

}