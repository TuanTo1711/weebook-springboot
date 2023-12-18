package org.weebook.api.web.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VoucherRequest {
    @NotBlank(message = "Description not blank")
    @Length(min = 5, message = "Enter a description that must be longer than 5 characters")
    private String description;


    //giảm % thì ghi %
    //giam tiền thì ghi money
    @NotBlank(message = "Type not blank")
    private String type;


    private LocalDateTime validTo = LocalDateTime.now().plus(7, ChronoUnit.DAYS);

    private LocalDateTime validFrom = LocalDateTime.now();


    @NotNull(message = "DiscountAmount not null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value \"DiscountAmount\" entered must be greater than 0.0")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value \"DiscountAmount\" entered must be less than 1000000000.0")
    private BigDecimal discountAmount;

    @NotNull(message = "Condition not null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value \"Condition\" entered must be greater than 0.0")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value \"Condition\" entered must be less than 1000000000.0")
    private BigDecimal condition;

    String code = UUID.randomUUID().toString();

    List<FilterRequest> filterRequest;
}
