package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.dto.mapper.CategoryMapper;
import org.weebook.api.entity.Category;
import org.weebook.api.repository.CategoryRepository;
import org.weebook.api.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository  categoryRepository;
    private  final CategoryMapper categoryMapper;
    @Override
    public List<CategoryDto> findAll() {
        List<Category> category = categoryRepository.findAll();
        List<Category> listCategory = category.stream().filter(c -> c.getParent() == null).toList();
        return  categoryMapper.listToDto(listCategory);
    }
}
