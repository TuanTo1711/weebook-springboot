package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusOrderRequest {

    Long idOrder;

    String status;
}
