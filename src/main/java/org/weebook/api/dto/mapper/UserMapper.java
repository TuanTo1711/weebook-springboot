package org.weebook.api.dto.mapper;


import org.mapstruct.*;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.entity.User;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "userInfo", source = "userDto")
    JwtResponse toJwtResponse(UserDto userDto, String token);

    UserDto toDto(User user);

    User toEntity(UserDto user);

    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "password", source = "request.password")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "role", source = "roleDto")
    User toEntity(SignUpRequest request, RoleDto roleDto);

    @InheritInverseConfiguration
    User partialUpdate(UserDto userDto);

    UpdateProfileResponse toProfileUpdated(UserDto userDto, UserDto dto);
}
