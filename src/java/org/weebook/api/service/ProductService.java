package org.weebook.api.service;

import org.springframework.data.domain.PageImpl;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);

    PageImpl<ProductDto> findAll(PagingRequest pagingRequest);

    ProductDto update(ProductDto productDto, Long id) ;

    PageImpl<ProductDto> saveListProduct(List<ProductDto> booksDTO);

    void delete(Long id) ;
}
