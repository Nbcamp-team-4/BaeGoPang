package com.team.project.domain.category.api;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.category.api.request.CreateCategoryRequest;
import com.team.project.domain.category.api.request.UpdateCategoryRequest;
import com.team.project.domain.category.api.response.CategoryResponse;
import com.team.project.domain.category.api.response.GetCategoriesResponse;
import com.team.project.domain.category.api.response.GetCategoryResponse;
import com.team.project.domain.category.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest request) {
        UUID userId = null; // TODO: JWT에서 추출
        return categoryService.createCategory(userId, request);
    }

    // 사용자용 목록(삭제 제외) - 페이징
    @GetMapping
    public GetCategoriesResponse getCategoriesForUser(@PageableDefault(size = 20) Pageable pageable) {
        return categoryService.getCategoriesForUser(pageable);
    }

    // 관리자용 목록(전체) - 페이징
    @GetMapping("/admin")
    public GetCategoriesResponse getCategoriesForAdmin(@PageableDefault(size = 20) Pageable pageable) {
        return categoryService.getCategoriesForAdmin(pageable);
    }

    // 단건 조회
    @GetMapping("/{categoryId}")
    public GetCategoryResponse getCategory(@PathVariable UUID categoryId) {
        return categoryService.getCategory(categoryId);
    }

    // 수정
    @PutMapping("/{categoryId}")
    public CategoryResponse update(@PathVariable UUID categoryId,
        @Valid @RequestBody UpdateCategoryRequest request) {
        UUID userId = null; // TODO: JWT에서 추출
        return categoryService.updateCategory(userId, categoryId, request);
    }

    // 삭제(soft delete)
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID categoryId) {
        UUID userId = null; // TODO: JWT에서 추출
        categoryService.deleteCategory(userId, categoryId);
    }
}