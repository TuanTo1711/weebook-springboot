package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.weebook.api.config.DefaultAppRole;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.User;
import org.weebook.api.service.AuthService;
import org.weebook.api.util.EmailSender;
import org.weebook.api.util.JwtUtil;
import org.weebook.api.util.OTPUtil;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInRequest;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userDetailsService;
    private final AuthenticationProvider daoAuthenticationProvider;
    private final UserMapper userMapper;
    private final EmailSender emailSender;
    private final OTPUtil otpUtil;
    private final JwtUtil jwtUtil;

    @Override
    public JwtResponse login(SignInRequest signInRequest) {
        Authentication unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(signInRequest.getUsername(), signInRequest.getPassword());

        Authentication authenticated = daoAuthenticationProvider.authenticate(unauthenticated);

        User user = (User) authenticated.getPrincipal();

        UserDto userDto = userMapper.toDto(user);
        return userMapper.toJwtResponse(userDto, jwtUtil.generateToken(authenticated));
    }

    @Override
    public UserDto register(SignUpRequest signUpRequest) {
        RoleDto roledto = DefaultAppRole.DEFAULT_USER_ROLE;
        User user = userMapper.toEntity(signUpRequest, roledto);
        String otp = otpUtil.generateOTP();
        user.setOtpCode(otp);
        emailSender.sendOTPEmail(user.getEmail(), otp);
        userDetailsService.createUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public UpdateProfileResponse update(UserDto userDto) {
        User entity = (User) userDetailsService.loadUserByUsername(userDto.username());
        userMapper.partialUpdate(userDto, entity);
        userDetailsService.updateUser(entity);
        return userMapper.toProfileUpdated(userDto, userMapper.toDto(entity));
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String oldPassword = changePasswordRequest.getCurrentPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        userDetailsService.changePassword(oldPassword, newPassword);
    }

    @Override
    public Boolean verifyOtp(String email, String code) {
        User entity = (User) userDetailsService.loadUserByUsername(email);
        String otpCode = entity.getOtpCode();

        Assert.state(otpCode.equals(code), "Otp code don't match");

        Instant otpExpiryTime = entity.getOtpExpiryTime();

        return Objects.nonNull(otpExpiryTime) && otpExpiryTime.isAfter(Instant.now());
    }

}
