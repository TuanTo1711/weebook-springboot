package org.weebook.api.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.dto.mapper.AdminMapper;
import org.weebook.api.admin.service.AdminService;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;
import org.weebook.api.config.DefaultAppRole;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsService;
    private final AdminMapper adminMapper;
    private final UserRepository userRepository;

    @Override
    public List<AdminDto> findAllStaff() {
        List<User> ListStaff = userRepository.findAll();
        return adminMapper.listToDto(ListStaff);
    }

    @Override
    public AdminDto registerStaff(AdminManageRoleRequest adminDtoRequest) {
        RoleDto selectRole = DefaultAppRole.getRoleConfigByUserType(adminDtoRequest.getRoleDto());
        User user = adminMapper.toEntity(adminDtoRequest, selectRole);
        userDetailsService.createUser(user);
        return adminMapper.toDto(user);
    }

    @Override
    public AdminUpdateStaffResponse updateStaff(AdminManageRoleRequest adminManageRoleRequest) {
        User entity = (User) userDetailsService.loadUserByUsername(adminManageRoleRequest.getUsername());
        AdminDto adminOld = adminMapper.toDto(entity);
        if (!StringUtils.isEmpty(adminManageRoleRequest.getPassword())){
            String encodePassword = passwordEncoder.encode(adminManageRoleRequest.getPassword());
            entity.setPassword(encodePassword);
        }
        RoleDto selectRole = DefaultAppRole.getRoleConfigByUserType(adminManageRoleRequest.getRoleDto());
        adminMapper.toEntity(adminManageRoleRequest, selectRole);
        User updatedUser = adminMapper.partialUpdate(adminManageRoleRequest, entity, selectRole);
        userDetailsService.updateUser(updatedUser);
        return adminMapper.updateStaff(adminOld, adminMapper.toDto(updatedUser));
    }

    @Override
    public List<AdminDto> getUserByRole(List<String> role) {
        List<User> userList = userRepository.findByRole_NameIn(role);
        return adminMapper.listToDto(userList);
    }
}
