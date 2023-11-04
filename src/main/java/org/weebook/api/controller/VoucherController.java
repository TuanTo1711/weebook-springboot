package org.weebook.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.service.VoucherService;
import org.weebook.api.web.request.VoucherRequest;

import java.util.List;

@RestController
@RequestMapping("api/v1/voucher")
@RequiredArgsConstructor
public class VoucherController {
    final VoucherService voucherService;


    @PostMapping("/create")
    VoucherDTO create(@Valid @RequestBody VoucherRequest voucherRequest){
        return voucherService.create(voucherRequest);
    }

    @GetMapping("/code")
    VoucherDTO findByCode(@Param("code") String code){
        return voucherService.findByCode(code);
    }

    @GetMapping("/user/get/all")
    List<VoucherDTO> userGetAll(@Param("id") Long id){
        return voucherService.userGetAll(id);
    }

    @GetMapping("/admin/get/all")
    List<VoucherDTO> adminGetAll(@Param("page") Integer page){
        return voucherService.adminGetAll(page);
    }

    @DeleteMapping("/delete/{code}")
    String delete(@PathVariable("code") String code){
        return voucherService.delete(code);
    }
}
