package org.weebook.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Product}
 */
public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        Integer discount,
        String thumbnail,
        String productCode,
        String supplierName,
        Integer weight,
        String packageSize,
        String content,
        String publisher,
        String publishYear,
        String translator,
        String language,
        String chapter,
        Integer pageNumber,
        String brand,
        String origin,
        String madeIn,
        String color,
        String material,
        String formality,
        Integer quantity,
        Instant estimatedDate,
        String status,
        List<String> authors,
        List<String> genres,
        Set<String> images,
        Set<ReviewDto> reviews) implements Serializable {
    /**
     * DTO for {@link org.weebook.api.entity.Review}
     */

    public record ReviewDto(
            UserDto user,
            ProductDto product,
            Integer rating,
            String comment,
            Instant createdAt) implements Serializable {
    }
}