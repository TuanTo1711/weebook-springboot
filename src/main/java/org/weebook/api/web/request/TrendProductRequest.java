package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrendProductRequest {
    LocalDate dateMin;

    LocalDate dateMax;

    PagingRequest pagingRequest;
}
