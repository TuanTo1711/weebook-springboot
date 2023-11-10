package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.dto.GenreDto;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.service.CategoryService;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/find-all")
    public List<CategoryDto> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("/find-category-name")
    public PageImpl<ProductDto> findCategoryName(@RequestParam("name") String name , @ModelAttribute PagingRequest pagingRequest){
        return categoryService.findProductsByName(name,pagingRequest);
    }

    @GetMapping("/list-genres")
    public List<GenreDto> listGenres(@RequestParam("name") String name , @ModelAttribute PagingRequest pagingRequest){
        return categoryService.listGenres(name,pagingRequest);
    }


}
