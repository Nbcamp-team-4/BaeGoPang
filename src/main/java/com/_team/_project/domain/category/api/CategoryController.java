package com._team._project.domain.category.api;

import com._team._project.domain.category.service.CategoryService;

public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}

