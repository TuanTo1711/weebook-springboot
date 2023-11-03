package org.weebook.api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VoucherDTO implements Serializable {
    @Id
    String code;

    BigDecimal condition;

    BigDecimal discountAmount;

    Instant validFrom;

    Instant validTo;

    String description;

    public VoucherDTO(String code, BigDecimal condition, BigDecimal discountAmount, Instant validFrom, Instant validTo, String description) {
        this.code = code;
        this.condition = condition;
        this.discountAmount = discountAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.description = description;
    }
}
