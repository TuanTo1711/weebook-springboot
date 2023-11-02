package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.service.impl.ProductServiceImpl;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {
   private final ProductServiceImpl productService;

    @GetMapping("/find-all")
    public PageImpl<ProductDto> findAll(@ModelAttribute PagingRequest pagingRequest) {
        return productService.findAll(pagingRequest);
    }

    @PutMapping("/save")
    public ProductDto save(@RequestBody ProductDto productDto) {
        return productService.saveProduct(productDto);
    }

    @PostMapping("/save-list")
    public List<ProductDto> saveList(@RequestBody List<ProductDto> productDto) {
        return productService.saveListProduct(productDto);
    }

    @PostMapping("/filter-products")
    public PageImpl<ProductDto> filterProducts(@ModelAttribute PagingRequest pagingRequest, @RequestParam Long id) {
        return productService.filterProducts(id, pagingRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/search")
    public PageImpl<ProductDto> findByName(@ModelAttribute PagingRequest pagingRequest,
                                           @RequestParam String name) {
        return productService.findByName(pagingRequest, name);
    }

    @GetMapping("/suggest")
    public List<ProductDto> findByNameSuggest(@ModelAttribute PagingRequest pagingRequest, @RequestParam String name) {
        return productService.findByNameSuggest(name);
    }

}
