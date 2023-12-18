package org.weebook.api.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.dto.ProductDetail;
import org.weebook.api.dto.ProductInfo;
import org.weebook.api.dto.ReviewDto;
import org.weebook.api.entity.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDetail productInfo);

    @Mapping(target = "totalReviews", expression = "java(product.getReviews().size())")
    ProductInfo toInfo(Product product);

    List<ProductInfo> toInfos(List<Product> product);

    @Mapping(source = "category.id", target = "categoryId")
    ProductDetail toDetail(Product product);

    @AfterMapping
    default void linkReviews(@MappingTarget Product product) {
        product.getReviews().forEach(review -> review.setProduct(product));
    }

    @Mapping(target = "name", expression = "java( review.getUser().getFirstName() + \" \" + review.getUser().getLastName() )")
    @Mapping(target = "avatar", source = "user.avatarUrl")
    ReviewDto toReviewDto(Review review);

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(ProductDetail productInfo, @MappingTarget Product product);

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

    List<ProductInfo> listToInfo(List<Product> productDto);
}