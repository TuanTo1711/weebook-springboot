package org.weebook.api.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.dto.ProductDto;
import org.weebook.api.entity.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    @AfterMapping
    default void linkReviews(@MappingTarget Product product) {
        product.getReviews().forEach(review -> review.setProduct(product));
    }

    @Mapping(target = "name", expression = "java( review.getUser().getFirstName() + \" \" + review.getUser().getLastName() )")
    @Mapping(target = "avatar", source = "user.avatarUrl")
    ProductDto.ReviewDto toReviewDto(Review review);

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(ProductDto productDto, @MappingTarget Product product);

    default String mapToImage(ProductsImage image) {
        return image.getImageUrl();
    }

    default String mapToAuthorName(Author author) {
        return author.getName();
    }

    default String mapToGenreName(Genre genre) {
        return genre.getName();
    }

    @Mapping(target = "imageUrl", source = "imageUrl")
    ProductsImage mapToProductImage(String imageUrl);

    @Mapping(target = "name", source = "authorName")
    Author mapToProductAuthor(String authorName);

    @Mapping(target = "name", source = "genreName")
    Genre mapToProductGenre(String genreName);

    List<Product> listToEntity(List<ProductDto> productDto);

    List<ProductDto> listToDto(List<Product> productDto);
}