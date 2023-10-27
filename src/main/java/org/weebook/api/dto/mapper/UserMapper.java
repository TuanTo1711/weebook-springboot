package org.weebook.api.dto.mapper;



import org.mapstruct.*;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.entity.Role;
import org.weebook.api.entity.User;
import org.weebook.api.web.request.SignUpFormRequest;
import org.weebook.api.web.response.JwtResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "accessToken", source = "token")
    JwtResponse toJwtResponse(UserDto userDto, String token);

    @Mapping(target = "fullNameUser", expression = "java(user.getFirstName() +' '+ user.getLastName())")
    @Mapping(target = "usernameUser", source = "username")
    @Mapping(target = "emailUser", source = "email")
    UserDto toDto(User user);

    User fromDto(UserDto userDto);

    @Mapping(target = "username", source = "request.usernameUser")
    @Mapping(target = "gender", source = "request.genderUser")
    @Mapping(target = "firstName", source = "request.firstNameUser")
    @Mapping(target = "lastName", source = "request.lastNameUser")
    @Mapping(target = "password",source = "request.passwordUser")
    @Mapping(target = "role", source = "roleDto")
    User toEntity(SignUpFormRequest request, RoleDto roleDto);

    @InheritInverseConfiguration
    void updateAuthFromDto(UserDto userDto, @MappingTarget User user);


    @Mapping(target = "name", source = "name")
    @Mapping(target = "permissions", source = "permissions")
    Role partialUpdate(RoleDto roleDto);


    RoleDto toDto(Role role);
}
