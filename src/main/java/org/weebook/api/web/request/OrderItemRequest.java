package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;
import org.weebook.api.dto.ProductInfo;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductInfo product;
}
