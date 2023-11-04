package org.weebook.api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TKProductDto {
    @Id
    Long id;

    String name;

    String image;

    Long quantity;

    BigDecimal total;
}
