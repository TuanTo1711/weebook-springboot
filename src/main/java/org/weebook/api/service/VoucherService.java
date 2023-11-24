package org.weebook.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.web.request.AddVoucherVaoUserRequest;
import org.weebook.api.web.request.PagingRequest;
import org.weebook.api.web.request.VoucherRequest;

import java.util.List;

public interface VoucherService {
    VoucherDTO create(VoucherRequest voucherRequest);

    VoucherDTO create(AddVoucherVaoUserRequest addVoucherVaoUserRequest);

    void update(VoucherRequest voucherRequest);

    List<VoucherDTO> userGetAll();

    List<VoucherDTO> adminGetAll(PagingRequest pagingRequest);

    String delete(String code);

    VoucherDTO findByCode(String code);

}
