package org.weebook.api.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.dto.mapper.AdminMapper;
import org.weebook.api.admin.service.AdminService;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;
import org.weebook.api.config.DefaultAppRole;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.entity.User;
import org.weebook.api.service.OTPService;
import org.weebook.api.util.JwtUtils;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsService;
    private final AuthenticationProvider daoAuthenticationProvider;
    private final OTPService otpService;
    private final JwtUtils jwtUtils;
    private final AdminMapper adminMapper;
    @Override
    public AdminDto registerStaff(AdminManageRoleRequest adminDtoRequest) {
        RoleDto selectRole = DefaultAppRole.getRoleConfigByUserType(adminDtoRequest.getRoleDto());
        User user = adminMapper.toEntity(adminDtoRequest, selectRole);
        userDetailsService.createUser(user);
        otpService.generateAndSendOTP(user.getUsername());
        return adminMapper.toDto(user);
    }

    @Override
    public AdminUpdateStaffResponse updateStaff(AdminManageRoleRequest adminManageRoleRequest) {
        User entity = (User) userDetailsService.loadUserByUsername(adminManageRoleRequest.getUsername());
        AdminDto adminOld = adminMapper.toDto(entity);
        RoleDto selectRole = DefaultAppRole.getRoleConfigByUserType(adminManageRoleRequest.getRoleDto());
        adminMapper.toEntity(adminManageRoleRequest, selectRole);
        User updatedUser = adminMapper.partialUpdate(adminManageRoleRequest, entity, selectRole);
        userDetailsService.updateUser(updatedUser);
        return adminMapper.updateStaff(adminOld, adminMapper.toDto(updatedUser));
    }


}
