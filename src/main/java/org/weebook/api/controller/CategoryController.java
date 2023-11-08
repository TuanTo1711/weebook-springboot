package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.service.CategoryService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/find-all")
    @Cacheable("getAllCategory")
    public List<CategoryDto> getAllCategory() {
        return categoryService.loadAllHierarchyCategory();
    }

    @GetMapping("/find-all-by")
    @Cacheable(value = "getAllCategoryByName", key = "#name")
    public List<CategoryDto> getAllCategoryByName(@RequestParam(required = false) String name) {
        return categoryService.loadCategoryByName(name);
    }

}
