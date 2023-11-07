package org.weebook.api.service;

import org.springframework.data.domain.PageImpl;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);

    PageImpl<ProductDto> getAll(PagingRequest pagingRequest);

    ProductDto update(ProductDto productDto, Long id);

    List<ProductDto> saveListProduct(List<ProductDto> booksDTO);

    PageImpl<ProductDto> filterProducts(String categoryName, PagingRequest pagingRequest);

    List<ProductDto> findByName(String name);

    List<ProductDto> findByNameSuggest(String name);

    void delete(Long id);
}
