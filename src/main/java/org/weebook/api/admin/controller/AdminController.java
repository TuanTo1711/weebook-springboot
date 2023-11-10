package org.weebook.api.admin.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.service.AdminService;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;
import org.weebook.api.dto.UserDto;
import org.weebook.api.web.response.ResultResponse;

import java.util.List;

@RestController
@RequestMapping("api/v2/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/find-all")
    public List<AdminDto> findAll(){
        return adminService.findAllStaff();
    }

    @GetMapping("/find-all-by")
    public List<AdminDto> searchUsersByRoles(@RequestParam("role") List<String> roles) {
        return adminService.getUserByRole(roles);
    }

    @PostMapping("/create")
    public ResultResponse<AdminDto> signUpManage(@RequestBody AdminManageRoleRequest adminManageRoleRequest){
        AdminDto registerStaff = adminService.registerStaff(adminManageRoleRequest);
        return ResultResponse.<AdminDto>builder()
                .status(200)
                .message("Register Your Admin Successfully !")
                .data(registerStaff)
                .build();
    }

    @PutMapping("/update")
    public ResultResponse<AdminUpdateStaffResponse> updateManage(@RequestBody AdminManageRoleRequest adminDto){
        return ResultResponse.<AdminUpdateStaffResponse>builder()
                .status(200)
                .message("Successfully update")
                .data(adminService.updateStaff(adminDto))
                .build();
    }

}
