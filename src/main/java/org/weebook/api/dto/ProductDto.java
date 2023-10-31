package org.weebook.api.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Product}
 */
@Data
public class ProductDto implements Serializable {
    String name;
    BigDecimal price;
    Integer discount;
    String thumbnail;
    String productCode;
    String supplierName;
    Integer weight;
    String packageSize;
    String content;
    String publisher;
    String publishYear;
    String translator;
    String language;
    String chapter;
    Integer pageNumber;
    String brand;
    String origin;
    String madeIn;
    String color;
    String material;
    String formality;
    Integer quantity;
    Instant estimatedDate;
    String status;
    Instant createdDate;
    Instant updatedDate;
    Set<String> images;
}