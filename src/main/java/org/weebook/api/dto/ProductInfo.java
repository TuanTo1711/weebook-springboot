package org.weebook.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Product}
 */
public record ProductInfo(
        Long id,
        String name,
        BigDecimal price,
        Integer discount,
        Integer quantity,
        Instant estimatedDate,
        String status,
        Integer totalReviews,
        Set<String> images,
        String thumbnail,
        String chapter) implements Serializable {
}