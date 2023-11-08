package org.weebook.api.service;

import org.springframework.data.domain.PageImpl;
import org.weebook.api.dto.ProductDetail;
import org.weebook.api.dto.ProductInfo;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

public interface ProductService {
    ProductDetail saveProduct(ProductDetail productInfo);

    ProductDetail update(ProductDetail productInfo, Long id);

    PageImpl<ProductInfo> filterProducts(String parent, String categoryName, PagingRequest pagingRequest);

    List<ProductInfo> search(String name);

    List<String> suggest(String name);

    void delete(Long id);

    ProductDetail findByName(String name);
}
