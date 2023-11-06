package org.weebook.api.web.request;

import lombok.Getter;
import lombok.Setter;
import org.weebook.api.dto.VoucherDTO;

import java.util.List;

@Getter
@Setter
public class AddVoucherVaoUserRequest {
    VoucherDTO voucherDTO;

    List<FilterRequest> filterRequest;

}
