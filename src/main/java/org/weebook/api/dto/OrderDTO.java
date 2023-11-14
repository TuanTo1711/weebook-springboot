package org.weebook.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private BigDecimal discountBalance;
    private BigDecimal discountVoucher;
    private String deliveryAddress;
    private UserDto user;

    private String name;

    private String phone;
}
