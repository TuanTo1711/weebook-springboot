package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    Long product_id;

    Integer quantity;

    BigDecimal unitPrice;


}
