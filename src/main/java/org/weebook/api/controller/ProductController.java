package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.ProductDetail;
import org.weebook.api.dto.ProductInfo;
import org.weebook.api.dto.mapper.ProductMapper;
import org.weebook.api.entity.Product;
import org.weebook.api.repository.ProductRepository;
import org.weebook.api.service.ProductService;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @PostMapping("/save")
    public ProductDetail saveProduct(@RequestBody ProductDetail productInfo) {
        return productService.saveProduct(productInfo);
    }

    @GetMapping("/find-all-by")
    public PageImpl<ProductInfo> filterProductsBy(
            @RequestParam(name = "parent", required = false) String parent,
            @RequestParam(name = "category", required = false) String categoryName,
            @ModelAttribute PagingRequest pagingRequest) {
        return productService.filterProducts(parent, categoryName, pagingRequest);
    }

    @GetMapping("/get-by-name")
    public ProductDetail getProductByName(@RequestParam("name") String name) {
        return productService.findByName(name);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/search")
    public List<ProductInfo> findByName(@RequestParam String name) {
        return productService.search(name);
    }

    @GetMapping("/suggest")
    public List<String> findByNameSuggest(@ModelAttribute PagingRequest pagingRequest, @RequestParam String name) {
        return productService.suggest(name);
    }

}
