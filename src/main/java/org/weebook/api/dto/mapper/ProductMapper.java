package org.weebook.api.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.entity.Product;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    List<Product> ListToEntity(List<ProductDto> productDto);
    List<ProductDto> ListToDto(List<Product> productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(ProductDto productDto, @MappingTarget Product product);


}