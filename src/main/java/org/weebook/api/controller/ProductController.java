package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.service.ProductService;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        return productService.saveProduct(productDto);
    }

    @PostMapping("/save-list")
    public List<ProductDto> saveList(@RequestBody List<ProductDto> productDto) {
        return productService.saveListProduct(productDto);
    }

    @GetMapping("/find-all-by")
    public PageImpl<ProductDto> filterProductsBy(
            @RequestParam(name = "category", required = false) String categoryName,
            @ModelAttribute PagingRequest pagingRequest) {
        return productService.filterProducts(categoryName, pagingRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/search")
    public List<ProductDto> findByName(@RequestParam String name) {
        return productService.findByName(name);
    }

    @GetMapping("/suggest")
    public List<ProductDto> findByNameSuggest(@ModelAttribute PagingRequest pagingRequest, @RequestParam String name) {
        return productService.findByNameSuggest(name);
    }

}
