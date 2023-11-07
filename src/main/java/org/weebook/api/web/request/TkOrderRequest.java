package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TkOrderRequest {
    String yearMonth;

    String nameProduct = "";

    Integer page = 0;
}
