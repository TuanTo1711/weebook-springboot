package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String shippingMethod;

    private String code;

    private Long user_id;

    private BigDecimal discount_balance;

    List<OrderItemRequest> orderItemRequests;
}
