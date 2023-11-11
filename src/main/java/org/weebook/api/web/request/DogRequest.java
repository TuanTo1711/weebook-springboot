package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DogRequest {
    LocalDate dateMin;
    LocalDate dateMax;

    //Trong 1 khoản thờ gian số lượng người dùng hủy đơn hàng tối đa.
    Integer max;

    PagingRequest pagingRequest;
}
