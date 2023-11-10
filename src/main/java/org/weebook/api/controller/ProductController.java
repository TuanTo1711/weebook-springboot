package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.service.impl.ProductServiceImpl;
import org.weebook.api.web.request.PagingRequest;

import java.io.IOException;
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

    @PostMapping("/save")
    public ProductDto save(@RequestBody ProductDto productDto) {
        return productService.saveProduct(productDto);
    }

    @PostMapping("/save-list")
    public List<ProductDto> saveList(@RequestPart("file") MultipartFile file) throws IOException {
        return productService.saveListProduct(file);
    }

    @PutMapping("/update")
    public ProductDto update(@RequestBody ProductDto productDto, @RequestParam Long id) {
        return productService.update(productDto, id);
    }

    @PostMapping("/filter-products")
    public PageImpl<ProductDto> filterProducts(@ModelAttribute PagingRequest pagingRequest, @RequestParam Long id) {
        return productService.filterProducts(id, pagingRequest);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        productService.delete(id);
    }

    @GetMapping ("/search")
    public PageImpl<ProductDto> findByName(@ModelAttribute PagingRequest pagingRequest,
                                           @RequestParam String name) {
        return productService.findByName(pagingRequest, name);
    }

    @GetMapping("/suggest")
    public List<ProductDto> findByNameSuggest(@ModelAttribute PagingRequest pagingRequest, @RequestParam String name) {
        return productService.findByNameSuggest(name);
    }

}
