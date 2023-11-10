package org.weebook.api.admin.service;

import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;
import org.weebook.api.entity.User;

import java.util.List;

public interface AdminService {
    List<AdminDto> findAllStaff ();
    AdminDto registerStaff (AdminManageRoleRequest adminManageRoleRequest);
    AdminUpdateStaffResponse updateStaff (AdminManageRoleRequest adminManageRoleRequest);
    List<AdminDto> getUserByRole(List<String> role);
}
