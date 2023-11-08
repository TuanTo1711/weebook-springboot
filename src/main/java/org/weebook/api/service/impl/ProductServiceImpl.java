package org.weebook.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.weebook.api.dto.ProductDetail;
import org.weebook.api.dto.ProductInfo;
import org.weebook.api.dto.mapper.ProductMapper;
import org.weebook.api.entity.Category;
import org.weebook.api.entity.Product;
import org.weebook.api.repository.CategoryRepository;
import org.weebook.api.repository.ProductRepository;
import org.weebook.api.service.ProductService;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;
import java.util.Optional;

import static org.weebook.api.util.CriteriaUtility.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDetail saveProduct(ProductDetail productInfo) {
        Product product = productRepository.save(productMapper.toEntity(productInfo));
        return productMapper.toDetail(product);
    }

    @Override
    public ProductDetail update(ProductDetail productInfo, Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            productMapper.partialUpdate(productInfo, product);
            return productMapper.toDetail(productRepository.save(product));
        }
        return null;
    }

    @Override
    @Cacheable(value = "filterProducts", key = "#parent + #categoryName + #pagingRequest.toString()")
    public PageImpl<ProductInfo> filterProducts(String parent, String categoryName, PagingRequest pagingRequest) {
        List<Category> withRecursive = categoryRepository.findWithRecursive(categoryName, parent);
        Specification<Product> spec = productsByCategoryName(withRecursive)
                .and(getSpecification(pagingRequest.getFilters()));
        Page<Product> page = productRepository.findAll(spec, buildPageable(pagingRequest));

        return (PageImpl<ProductInfo>) page.map(productMapper::toInfo);
    }

    @Override
    @Cacheable(value = "search", key = "#name")
    public List<ProductInfo> search(String name) {
        Specification<Product> specification = buildFieldSearch("name", name);
        var products = productRepository.findAll(specification, PageRequest.of(0, 10));
        return productMapper.listToInfo(products.getContent());
    }

    @Override
    @Cacheable(value = "suggest", key = "#name")
    public List<String> suggest(String name) {
        Specification<Product> specification = buildFieldLikeLeading("name", name);
        var products = productRepository.findAll(specification, PageRequest.of(0, 10));
        return products.getContent().stream()
                .map(Product::getName)
                .toList();
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "findByName", key = "#name")
    public ProductDetail findByName(String name) {
        List<Product> products = productRepository.findAll(buildFieldSlug("name", name));
        Assert.notEmpty(products, "Not found by: " + name);
        return productMapper.toDetail(products.get(0));
    }

    private Pageable buildPageable(PagingRequest pagingRequest) {
        return PageRequest.of(
                pagingRequest.getPageNumber(),
                pagingRequest.getPageSize(),
                buildSort(pagingRequest.getSortBy(), pagingRequest.getSortType())
        );
    }
}
