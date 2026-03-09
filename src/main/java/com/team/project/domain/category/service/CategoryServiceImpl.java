package com.team.project.domain.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.category.api.request.CategoryPageRequest;
import com.team.project.domain.category.api.request.CreateCategoryRequest;
import com.team.project.domain.category.api.request.UpdateCategoryRequest;
import com.team.project.domain.category.api.response.CategoryResponse;
import com.team.project.domain.category.api.response.GetCategoriesResponse;
import com.team.project.domain.category.api.response.GetCategoryResponse;
import com.team.project.domain.category.entity.Category;
import com.team.project.domain.category.exception.CategoryDuplicateException;
import com.team.project.domain.category.exception.CategoryNotFoundException;
import com.team.project.domain.category.repository.CategoryRepository;
import com.team.project.domain.store.entity.StoreCategory;
import com.team.project.domain.store.repository.StoreCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    @Override
    public CategoryResponse createCategory(UserDto userDto, CreateCategoryRequest request) {
        if (categoryRepository.existsByNameAndDeletedAtIsNull(request.getName())) {
            throw new CategoryDuplicateException();
        }

        Category category = Category.builder()
            .name(request.getName())
            .createdBy(userDto.getId())
            .build();

        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCategoryResponse getCategory(UUID categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        return GetCategoryResponse.of(CategoryResponse.from(category));
    }

    /**
     * 사용자용(삭제 제외) 페이징 조회
     */
    @Override
    @Transactional(readOnly = true)
    public GetCategoriesResponse getCategoriesForUser(CategoryPageRequest request) {

        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

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
    public GetCategoriesResponse getCategoriesForAdmin(CategoryPageRequest request) {
        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

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
    public CategoryResponse updateCategory(UserDto userDto, UUID categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        boolean nameChanged = !category.getName().equals(request.getName());
        if (nameChanged && categoryRepository.existsByNameAndDeletedAtIsNull(request.getName())) {
            throw new CategoryDuplicateException();
        }

        category.update(request.getName(), userDto.getId());
        return CategoryResponse.from(category);
    }

    @Override
    public void deleteCategory(UserDto userDto, UUID categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        List<StoreCategory> mappings =
            storeCategoryRepository.findAllByCategory_IdAndDeletedAtIsNull(categoryId);

        for (StoreCategory mapping : mappings) {
            mapping.markDeleted(userDto.getId());
        }

        category.markDeleted(userDto.getId());
    }
}