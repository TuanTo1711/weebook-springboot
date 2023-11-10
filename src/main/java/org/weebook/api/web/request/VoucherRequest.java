package org.weebook.api.web.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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


    @NotNull(message = "ValidTo not null")
    @FutureOrPresent(message = "The expiration date must be a future date")
    private LocalDateTime validTo;

    @NotNull(message = "ValidFrom not null")
    @FutureOrPresent(message = "The expiration date must be a future date")
    private LocalDateTime validFrom;


    @NotNull(message = "DiscountAmount not null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value \"DiscountAmount\" entered must be greater than 0.0")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value \"DiscountAmount\" entered must be less than 1000000000.0")
    private BigDecimal discountAmount;

    @NotNull(message = "Condition not null")
    @DecimalMin(value = "0.0", inclusive = false, message = "The value \"Condition\" entered must be greater than 0.0")
    @DecimalMax(value = "1000000000.0", inclusive = false, message = "The value \"Condition\" entered must be less than 1000000000.0")
    private BigDecimal condition;

    List<FilterRequest> filterRequest;
}
