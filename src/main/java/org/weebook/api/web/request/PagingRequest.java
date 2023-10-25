package org.weebook.api.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>This object will be needed when you want to send request include pagination to the API</p>
 * Using {@link ModelAttribute} annotation in method parameter to injection into controller
 *
 * @author Tô Hoàng Tuấn - Yuuta
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingRequest implements Serializable {
    @NotNull(message = "Page number must be not null !!!")
    @NotEmpty(message = "Page number must be not empty !!!")
    @NotBlank(message = "Page number must be not blank !!!")
    @Min(value = 0, message = "Page number must be greater than or equal to {value}")
    @Builder.Default
    private Integer pageNumber = 0;

    @NotNull(message = "Page size must be not null !!!")
    @NotEmpty(message = "Page size must be not empty !!!")
    @NotBlank(message = "Page size must be not blank !!!")
    @Min(value = 0, message = "Page size must be greater than or equal to {value}")
    @Builder.Default
    private Integer pageSize = 48;
    @Builder.Default
    private String sortBy = "createdDate";
    @Builder.Default
    private String sortType = "asc";
    @Builder.Default
    private transient List<FilterRequest> filters = new LinkedList<>();
}