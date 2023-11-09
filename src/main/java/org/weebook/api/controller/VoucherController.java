package org.weebook.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.service.VoucherService;
import org.weebook.api.web.request.AddVoucherVaoUserRequest;
import org.weebook.api.web.request.PagingRequest;
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

    @PostMapping("add/create")
    VoucherDTO create(@RequestBody AddVoucherVaoUserRequest addVoucherVaoUserRequest){
        return voucherService.create(addVoucherVaoUserRequest);
    }

    @GetMapping("/code")
    VoucherDTO findByCode(@Param("code") String code){
        return voucherService.findByCode(code);
    }

    @PostMapping("/user/get/all")
    List<VoucherDTO> userGetAll(){
        return voucherService.userGetAll();
    }

    @PostMapping("/admin/get/all")
    List<VoucherDTO> adminGetAll(@RequestBody PagingRequest pagingRequest){
        return voucherService.adminGetAll(pagingRequest);
    }

    @DeleteMapping("/delete/{code}")
    String delete(@PathVariable("code") String code){
        return voucherService.delete(code);
    }
}
