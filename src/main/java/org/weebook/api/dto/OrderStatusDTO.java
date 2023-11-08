package org.weebook.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class OrderStatusDTO {
    private String status;
    private Instant statusDate;
}
