package org.weebook.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Product}
 */
public record ProductDetail(
        Long id, String name, BigDecimal price, Integer discount, String thumbnail, String productCode,
        String supplierName, Integer weight, String packageSize, String content, String publisher,
        String publishYear, String translator, String language, String chapter, Integer pageNumber,
        String brand, String origin, String madeIn, String color, String material, String formality,
        Integer quantity, Instant estimatedDate, String status, String seriesName,
        Set<String> authors,
        Set<String> genres,
        Set<String> images,
        Set<ReviewDto> reviews) implements Serializable {
}