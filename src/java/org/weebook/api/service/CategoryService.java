package org.weebook.api.service;

import org.weebook.api.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
}
