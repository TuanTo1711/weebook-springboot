package org.weebook.api.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.weebook.api.entity.Review}
 */

public record ReviewDto(
        String name,
        String avatar,
        Integer rating,
        String comment,
        Instant createdAt) implements Serializable {
}