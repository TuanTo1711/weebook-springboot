package org.weebook.api.service;

import org.springframework.data.domain.PageImpl;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.dto.GenreDto;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    PageImpl<ProductDto> findProductsByName(String name, PagingRequest pagingRequest);


    List<GenreDto> listGenres(String name, PagingRequest pagingRequest);
}
