package org.weebook.api.web.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "Totalamount not null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value \"Totalamount\" entered must be greater than 0.0")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value \"Totalamount\" entered must be less than 1000000000.0")
    private BigDecimal totalAmount;

    @NotBlank(message = "Delivery Address not blank")
    private String deliveryAddress;
    @NotBlank(message = "ShippingMethod not blank")
    private String shippingMethod;

    private String code;

    @NotNull(message = "Chưa login")
    private Long user_id;

    private BigDecimal discount_balance = BigDecimal.ZERO;

    List<OrderItemRequest> orderItemRequests;
}
