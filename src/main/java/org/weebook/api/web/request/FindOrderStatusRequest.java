package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindOrderStatusRequest {
    private String status;

    private PagingRequest pagingRequest;
}
