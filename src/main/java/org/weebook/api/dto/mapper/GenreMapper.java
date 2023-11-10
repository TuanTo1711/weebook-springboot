package org.weebook.api.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.dto.GenreDto;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.entity.Genre;
import org.weebook.api.entity.Product;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    Genre toEntity(GenreDto genreDto);

    GenreDto toDto(Genre genre);

    List<Genre> ListToEntity(List<GenreDto> productDto);
    List<GenreDto> ListToDto(List<Genre> productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Genre partialUpdate(GenreDto genreDto, @MappingTarget Genre genre);
}