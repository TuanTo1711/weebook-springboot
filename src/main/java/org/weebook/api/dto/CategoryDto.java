package org.weebook.api.dto;

import java.io.Serializable;
import java.util.List;

public record CategoryDto(
        Long id,
        String name,
        List<CategoryDto> children) implements Serializable {
}