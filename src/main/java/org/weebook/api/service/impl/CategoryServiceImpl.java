package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.dto.mapper.CategoryMapper;
import org.weebook.api.entity.Category;
import org.weebook.api.repository.CategoryRepository;
import org.weebook.api.service.CategoryService;
import org.weebook.api.util.CriteriaUtility;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> loadAllHierarchyCategory() {
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .filter(c -> Objects.isNull(c.getParent()))
                .toList();

        return categoryMapper.listToDto(categories);
    }

    @Override
    public List<CategoryDto> loadCategoryByName(String name) {
        Specification<Category> specification = CriteriaUtility.buildFieldSlug("name", name);
        List<Category> categories = categoryRepository.findAll(specification);
        return categoryMapper.listToDto(categories);
    }
}
