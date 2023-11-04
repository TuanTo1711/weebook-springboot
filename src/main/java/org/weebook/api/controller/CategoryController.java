package org.weebook.api.controller;

import lombok.AllArgsConstructor;
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
    public List<CategoryDto> getAllCategory() {
        return categoryService.loadAllHierarchyCategory();
    }

    @GetMapping("/find-all-by")
    public List<CategoryDto> getAllCategory(@RequestParam(required = false) String name) {
        return categoryService.loadCategoryByName(name);
    }

}
