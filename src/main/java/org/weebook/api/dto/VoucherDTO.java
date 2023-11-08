package org.weebook.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class VoucherDTO implements Serializable {
    String code;

    BigDecimal condition;

    BigDecimal discountAmount;

    Instant validFrom;

    Instant validTo;

    String description;

}
