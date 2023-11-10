package org.weebook.api.web.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OrderFeedBackRequest {
    @NotNull(message = "Rating not null")
    @Min(value = 1, message = "The value entered must be greater than 1")
    @Max(value = 5, message = "The value entered must be less than 5")
    private Integer rating;

    @NotBlank(message = "ReviewText Address not blank")
    private String reviewText;

    @NotNull(message = "IDOrder not null")
    private Long idOrder;

    private Set<String> images;
}
