package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.dto.GenreDto;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.dto.mapper.CategoryMapper;
import org.weebook.api.dto.mapper.GenreMapper;
import org.weebook.api.dto.mapper.ProductMapper;
import org.weebook.api.entity.Category;
import org.weebook.api.entity.Genre;
import org.weebook.api.entity.Product;
import org.weebook.api.repository.CategoryRepository;
import org.weebook.api.repository.ProductRepository;
import org.weebook.api.repository.genres;
import org.weebook.api.service.CategoryService;
import org.weebook.api.util.CriteriaUtility;
import org.weebook.api.web.request.FilterRequest;
import org.weebook.api.web.request.PagingRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository  categoryRepository;
    private  final CategoryMapper categoryMapper;
    private  final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private  final genres genres;
    private  final GenreMapper genreMapper;

    @Override
    public List<CategoryDto> findAll() {
        List<Category> category = categoryRepository.findAll();
        List<Category> listCategory = category.stream().filter(c -> c.getParent() == null).toList();
        return  categoryMapper.listToDto(listCategory);
    }
    @Override
    public PageImpl<ProductDto> findProductsByName(String name , PagingRequest pagingRequest) {
        List<Category> categories = categoryRepository.findByName(name);
        List<Object> array = new ArrayList<>();
        for (Category category : categories) {
            List<Long> categoryIds = collectCategoryIds(category);
            array.addAll(categoryIds);
        }
        FilterRequest filterRequest = new FilterRequest("category.id", null, "IN", array);
        pagingRequest.getFilters().add(filterRequest);
        Specification<Product> specification = CriteriaUtility.getSpecification(pagingRequest.getFilters());
        Page<Product> products = productRepository
                .findAll(specification, PageRequest.of(0,25));
        return (PageImpl<ProductDto>) products.map(productMapper::toDto);
    }

    @Override
    public List<GenreDto> listGenres(String name, PagingRequest pagingRequest) {
        List<Category> categories = categoryRepository.findByName(name);
        List<Object> array = new ArrayList<>();
        for (Category category : categories) {
            List<Long> categoryIds = collectCategoryIds(category);
            array.addAll(categoryIds);
        }
        List<Genre> genrePage =  genres.listGenresByCategoryName(array);

        return genreMapper.ListToDto(genrePage);

    }

    public List<Long> collectCategoryIds(Category category){
        List<Long> longs = new ArrayList<>();
        longs.add(category.getId());

        for (Category child : category.getChildren()) {
            List<Long> collectCategoryIds = collectCategoryIds(child);
            longs.addAll(collectCategoryIds);
        }
        return longs;
    }

}