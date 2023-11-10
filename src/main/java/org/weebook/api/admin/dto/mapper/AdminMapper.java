package org.weebook.api.admin.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.admin.dto.AdminDto;
import org.weebook.api.admin.web.request.AdminManageRoleRequest;
import org.weebook.api.admin.web.response.AdminUpdateStaffResponse;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdminMapper {


    @Mapping(target = "role", source = "roleDto")
    User toEntity(AdminManageRoleRequest adminDto, RoleDto roleDto);

    AdminDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", source = "selectRole")
    User partialUpdate(AdminManageRoleRequest adminDto, @MappingTarget User user, RoleDto selectRole);

    @Mapping(target = "old", source = "adminDto")
    @Mapping(target = "neq", source = "dto")
    AdminUpdateStaffResponse updateStaff(AdminDto adminDto, AdminDto dto);
}