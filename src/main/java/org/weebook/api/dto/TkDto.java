package org.weebook.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TkDto {
    List<TKProductDto> productDto;

    BigDecimal total;
}
