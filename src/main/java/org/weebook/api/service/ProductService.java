package org.weebook.api.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.web.request.PagingRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);

    PageImpl<ProductDto> findAll(PagingRequest pagingRequest);

    ProductDto update(ProductDto productDto, Long id) ;

    List<ProductDto> saveListProduct(MultipartFile file) throws IOException;

    PageImpl<ProductDto> filterProducts(Long id, PagingRequest pagingRequest);

    PageImpl<ProductDto> findByName(PagingRequest pagingRequest,String name);

    List<ProductDto> findByNameSuggest(String name);




    void delete(Long id) ;
}
