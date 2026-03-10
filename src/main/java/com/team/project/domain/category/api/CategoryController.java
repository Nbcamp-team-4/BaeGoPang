package com.team.project.domain.category.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.category.api.request.CategoryPageRequest;
import com.team.project.domain.category.api.request.CreateCategoryRequest;
import com.team.project.domain.category.api.request.UpdateCategoryRequest;
import com.team.project.domain.category.api.response.AdminCategoryResponse;
import com.team.project.domain.category.api.response.CategoryResponse;
import com.team.project.domain.category.api.response.GetCategoryResponse;
import com.team.project.domain.category.service.CategoryService;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 등록")
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
        @CurrentUser UserDto userDto,
        @RequestBody CreateCategoryRequest request
    ) {
        return categoryService.createCategory(userDto, request);
    }

    @Operation(summary = "카테고리 단건 조회")
    @GetMapping("/{categoryId}")
    public GetCategoryResponse getCategory(@PathVariable UUID categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping
    public BasePageResponse<CategoryResponse> getCategories(
        @ModelAttribute CategoryPageRequest request
    ) {
        return categoryService.getCategories(request);
    }

    @Operation(summary = "관리자용 카테고리 목록 조회")
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public BasePageResponse<AdminCategoryResponse> getCategoriesForAdmin(
        @ModelAttribute CategoryPageRequest request
    ) {
        return categoryService.getCategoriesForAdmin(request);
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public CategoryResponse updateCategory(
        @CurrentUser UserDto userDto,
        @PathVariable UUID categoryId,
        @RequestBody UpdateCategoryRequest request
    ) {
        return categoryService.updateCategory(userDto, categoryId, request);
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
        @CurrentUser UserDto userDto,
        @PathVariable UUID categoryId
    ) {
        categoryService.deleteCategory(userDto, categoryId);
    }
}