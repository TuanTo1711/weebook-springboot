package org.weebook.api.dto;

import lombok.Data;
import lombok.Value;
import org.weebook.api.entity.Category;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryDto implements Serializable {
    Long id;
    String name;
    List<CategoryDto> children;
}