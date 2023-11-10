package org.weebook.api.projection;


import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderStatusProjection {
    String getStatus();
    Instant getStatusDate();

    OrderProjection getOrder();

    interface OrderProjection{
        Long getId();
        Instant getOrderDate();

        BigDecimal getTotalAmount();

        BigDecimal getDiscountBalance();

        BigDecimal getDiscountVoucher();

        String getDeliveryAddress();

        String getShippingMethod();

        Instant getDeliveryDate();


        String getDeliveryStatus();

        String getStatus();

        UserProjection getUser();
    }

    interface UserProjection{
        String getFirstName();

        String getLastName();

        String getUsername();


        String getEmail();
    }
}
