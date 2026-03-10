package com.team.project.domain.category.api;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 API")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성
     */
    @Operation(summary = "카테고리 생성", description = "관리자(MASTER, MANAGER)가 카테고리를 생성합니다.")
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest request) {

        UUID userId = null; // TODO: Security 적용 후 JWT에서 추출

        return categoryService.createCategory(userId, request);
    }

    /**
     * 사용자용 카테고리 목록 조회
     */
    @Operation(summary = "카테고리 목록 조회 (사용자)", description = "삭제되지 않은 카테고리 목록을 조회합니다.")
    @GetMapping
    public GetCategoriesResponse getCategoriesForUser(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return categoryService.getCategoriesForUser(pageable);
    }

    /**
     * 관리자용 카테고리 목록 조회
     */
    @Operation(summary = "카테고리 목록 조회 (관리자)", description = "관리자가 전체 카테고리 목록을 조회합니다.")
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public GetCategoriesResponse getCategoriesForAdmin(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return categoryService.getCategoriesForAdmin(pageable);
    }

    /**
     * 카테고리 단건 조회
     */
    @Operation(summary = "카테고리 조회", description = "카테고리 정보를 조회합니다.")
    @GetMapping("/{categoryId}")
    public GetCategoryResponse getCategory(
        @PathVariable UUID categoryId
    ) {
        return categoryService.getCategory(categoryId);
    }

    /**
     * 카테고리 수정
     */
    @Operation(summary = "카테고리 수정", description = "관리자(MASTER, MANAGER)가 카테고리를 수정합니다.")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public CategoryResponse update(
        @PathVariable UUID categoryId,
        @Valid @RequestBody UpdateCategoryRequest request
    ) {

        UUID userId = null; // TODO: Security 적용 후 JWT에서 추출

        return categoryService.updateCategory(userId, categoryId, request);
    }

    /**
     * 카테고리 삭제 (Soft Delete)
     */
    @Operation(summary = "카테고리 삭제", description = "관리자(MASTER, MANAGER)가 카테고리를 삭제합니다.")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @PathVariable UUID categoryId
    ) {

        UUID userId = null; // TODO: Security 적용 후 JWT에서 추출

        categoryService.deleteCategory(userId, categoryId);
    }
}