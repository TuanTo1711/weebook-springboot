package org.weebook.api.service;

import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.web.request.VoucherRequest;

import java.util.List;

public interface VoucherService {
    VoucherDTO create(VoucherRequest voucherRequest);

    List<VoucherDTO> userGetAll(Long id);

    List<VoucherDTO> adminGetAll(Integer page);

    String delete(String code);

    VoucherDTO findByCode(String code);

}
