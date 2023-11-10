package org.weebook.api.admin.service;

import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;

public interface AdminService {
    AdminDto registerStaff (AdminManageRoleRequest adminManageRoleRequest);

    AdminUpdateStaffResponse updateStaff (AdminManageRoleRequest adminManageRoleRequest);

}
