package org.weebook.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OrderFeedBackDto {
    private Integer rating;
    private String reviewText;
    private Set<String> images = null;
}
