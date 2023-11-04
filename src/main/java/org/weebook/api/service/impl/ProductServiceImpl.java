package org.weebook.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.dto.mapper.ProductMapper;
import org.weebook.api.entity.Product;
import org.weebook.api.repository.ProductRepository;
import org.weebook.api.service.ProductService;
import org.weebook.api.util.CriteriaUtility;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;
import java.util.Optional;

import static org.weebook.api.util.CriteriaUtility.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = productRepository.save(productMapper.toEntity(productDto));
        return productMapper.toDto(product);
    }

    @Override
    public PageImpl<ProductDto> getAll(PagingRequest pagingRequest) {
        Specification<Product> specification = getSpecification(pagingRequest.getFilters());
        Page<Product> page = productRepository.findAll(specification, buildPageable(pagingRequest));
        return (PageImpl<ProductDto>) page.map(productMapper::toDto);
    }

    @Override
    public ProductDto update(ProductDto productDto, Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            productMapper.partialUpdate(productDto, product);
            return productMapper.toDto(productRepository.save(product));
        }
        return null;
    }

    @Override
    public List<ProductDto> saveListProduct(List<ProductDto> booksDTO) {
        List<Product> productList = productMapper.listToEntity(booksDTO);
        return productMapper.listToDto(productRepository.saveAll(productList));
    }

    @Override
    @Cacheable(value = "filterProducts", key = "#categoryName + #pagingRequest.toString()")
    public PageImpl<ProductDto> filterProducts(String categoryName, PagingRequest pagingRequest) {
        Specification<Product> specification = CriteriaUtility
                .<Product>buildFieldSlug("category.name", categoryName)
                .and(getSpecification(pagingRequest.getFilters()));
        Page<Product> products = productRepository.findAll(specification, buildPageable(pagingRequest));
        return (PageImpl<ProductDto>) products.map(productMapper::toDto);
    }

    @Override
    public List<ProductDto> findByName(String name) {
        Specification<Product> specification = buildFieldSearch("name", name);
        var products = productRepository.findAll(specification);
        return productMapper.listToDto(products);
    }

    @Override
    public List<ProductDto> findByNameSuggest(String name) {
        Specification<Product> specification = buildFieldLikeLeading("name", name);
        List<Product> products = productRepository.findAll(specification);
        return productMapper.listToDto(products);
    }


    private Pageable buildPageable(PagingRequest pagingRequest) {
        return PageRequest.of(
                pagingRequest.getPageNumber(),
                pagingRequest.getPageSize(),
                buildSort(pagingRequest.getSortBy(), pagingRequest.getSortType())
        );
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
