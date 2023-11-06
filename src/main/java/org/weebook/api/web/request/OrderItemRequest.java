package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;
import org.weebook.api.dto.ProductDto;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    ProductDto product;

    Integer quantity;

    BigDecimal unitPrice;


}
