package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.weebook.api.config.DefaultAppRole;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.service.AuthService;
import org.weebook.api.service.OTPService;
import org.weebook.api.util.JwtUtils;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInRequest;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userDetailsService;
    private final AuthenticationProvider daoAuthenticationProvider;
    private final UserMapper userMapper;
    private final OTPService otpService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse login(SignInRequest signInRequest) {
        Authentication unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(signInRequest.getUsername(), signInRequest.getPassword());

        Authentication authenticated = daoAuthenticationProvider.authenticate(unauthenticated);

        User user = (User) authenticated.getPrincipal();

        UserDto userDto = userMapper.toDto(user);
        return userMapper.toJwtResponse(userDto, jwtUtils.generateToken(authenticated));
    }

    @Override
    public UserDto register(SignUpRequest signUpRequest) {
        RoleDto roledto = DefaultAppRole.DEFAULT_ROLE_CONFIG;
        User user = userMapper.toEntity(signUpRequest, roledto);
        userDetailsService.createUser(user);
        otpService.generateAndSendOTP(user.getUsername());
        return userMapper.toDto(user);
    }

    @Override
    public UpdateProfileResponse updateProfile(UserDto userDto) {
        User entity = (User) userDetailsService.loadUserByUsername(userDto.username());
        UserDto userDtoOld = userMapper.toDto(entity);
        userMapper.partialUpdate(userDto, entity);
        userDetailsService.updateUser(entity);
        return userMapper.toProfileUpdated(userDtoOld, userMapper.toDto(entity));
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String oldPassword = changePasswordRequest.getCurrentPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        userDetailsService.changePassword(oldPassword, newPassword);
    }

    @Override
    public JwtResponse removeAuth(SignInRequest signInRequest) {
        return null;
    }

}
