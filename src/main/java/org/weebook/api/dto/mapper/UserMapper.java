package org.weebook.api.dto.mapper;


import org.mapstruct.*;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.entity.User;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "userInfo", source = "userDto")
    JwtResponse toJwtResponse(UserDto userDto, String token);

    UserDto toDto(User user);

    List<UserDto> toDtos(List<User> user);

    User toEntity(UserDto user);

    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "password", source = "request.password")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "role", source = "roleDto")
    User toEntity(SignUpRequest request, RoleDto roleDto);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    void partialUpdate(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "userOld" , source = "userDto")
    @Mapping(target = "userNew", source = "dto")
    UpdateProfileResponse toProfileUpdated(UserDto userDto, UserDto dto);
}
