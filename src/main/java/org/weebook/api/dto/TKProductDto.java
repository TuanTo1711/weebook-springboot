package org.weebook.api.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

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


    public TKProductDto(Long id, String name, String image, Long quantity, BigDecimal total) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.total = total;
    }
}
