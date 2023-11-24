package org.weebook.api.web.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.weebook.api.dto.ProductInfo;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "Total Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value  entered must be greater than {value}")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value entered must be less than {value}")
    private BigDecimal totalAmount;

    @NotNull(message = "Delivery address not be null")
    @NotBlank(message = "Delivery address not blank")
    private String deliveryAddress;
    @NotNull(message = "Shipping method not be null")
    @NotBlank(message = "Shipping method not blank")
    private String shippingMethod;

    @NotBlank(message = "Name not blank")
    String name;

    @NotBlank(message = "Phone address not blank")
    String phone;

    private String code;

    private BigDecimal discountBalance = BigDecimal.ZERO;

    private List<OrderItemRequest> orderItemRequests;
}
