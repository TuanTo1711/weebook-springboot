package org.weebook.api.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.weebook.api.entity.Order;
import org.weebook.api.entity.User;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class TransactionDto {

    private String action;

    private Instant actionTime;

    private OrderDTO order;

    private BigDecimal preTradeAmount;

    private BigDecimal amount;

    private BigDecimal postTradeAmount;


    private UserDto user;
}
