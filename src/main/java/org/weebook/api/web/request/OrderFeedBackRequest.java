package org.weebook.api.web.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class OrderFeedBackRequest {
    @NotNull(message = "Rating not null")
    @Min(value = 1, message = "The value \"Rating\" entered must be greater than 1")
    @Max(value = 5,message = "The value \"Rating\" entered must be less than 5")
    private Integer rating;

    @NotBlank(message = "ReviewText Address not blank")
    private String reviewText;

    @NotNull(message = "IDOrder not null")
    Long idOrder;

    private Set<String> images;
}
