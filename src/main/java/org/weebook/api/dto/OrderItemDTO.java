package org.weebook.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
    Integer quantity;

    private BigDecimal unitPrice;

    ProductDto product;
}
