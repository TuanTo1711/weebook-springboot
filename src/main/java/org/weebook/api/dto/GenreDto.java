package org.weebook.api.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.weebook.api.entity.Genre}
 */
@Value
public class GenreDto implements Serializable {
    Long id;
    String name;
    String description;
}