package org.weebook.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.weebook.api.entity.OrderFeedback;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class OrderDTO {
    Long id;
    private Instant orderDate;
    private BigDecimal totalAmount;

    private BigDecimal totalDiscount;

    private String deliveryAddress;

    UserDto user;

    private Set<OrderItemDTO> orderItems;

    Set<OrderStatusDTO> orderStatuses;


    OrderFeedBackDto orderFeedbacks;
}
