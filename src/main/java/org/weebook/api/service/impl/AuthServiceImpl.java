package org.weebook.api.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.weebook.api.config.RoleDtoConfig;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.User;
import org.weebook.api.exception.ErrorMessages;
import org.weebook.api.repository.RoleRepo;
import org.weebook.api.repository.UserRepo;
import org.weebook.api.service.AuthService;
import org.weebook.api.util.JwtUtils;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInFormRequest;
import org.weebook.api.web.request.SignUpFormRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.SignUpFormResponse;
import org.weebook.api.web.response.UpdateFormResponse;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final OTPServiceImpl otpService;


    @Override
    public JwtResponse loginAuth(SignInFormRequest signInFormRequest) throws Exception {

        User user = userRepo.findByUsername(signInFormRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_ERROR));
        if (!passwordEncoder.matches(signInFormRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or Password is not correct");
        }
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), null, user.getAuthorities());
        UserDto userDto = userMapper.toDto(user);
        return userMapper.toJwtResponse(
                userDto, jwtUtils.generateToken(authentication)
        );
    }

    @Override
    public SignUpFormResponse signUpAuth(SignUpFormRequest signUpFormRequest) throws Exception {
        Optional<User> existingUser = userRepo.findByUsername(signUpFormRequest.getUsernameUser());
        if (existingUser.isPresent()) {
            throw new Exception(" Sorry, this email is already registered. Please enter another email! ");
        }

        RoleDto roledto = RoleDtoConfig.DEFAULT_ROLE_CONFIG;
        String encodedPassword = passwordEncoder.encode(signUpFormRequest.getPasswordUser());
        signUpFormRequest.setPasswordUser(encodedPassword);


        User user = userMapper.toEntity(signUpFormRequest, roledto);
        User saveUser = userRepo.save(user);
        otpService.generateAndSendOTP(saveUser.getUsername());
        UserDto userDto = userMapper.toDto(saveUser);

        return SignUpFormResponse.builder()
                .userDto(userDto)
                .build();
    }

    @Override
    public UpdateFormResponse updateProfile(UserDto userDto, Long id) {
        Optional<User> existingId = userRepo.findById(id);
        if (!existingId.isPresent()) {
            throw new UsernameNotFoundException("Cannot find user with email " + userDto.emailUser() + "please try again");
        }
        User user = existingId.get();
        UserDto userDtoOld = userMapper.toDto(user);

        userMapper.updateAuthFromDto(userDto, user);

        userRepo.save(user);
        UserDto updateUser = userMapper.toDto(user);

        return UpdateFormResponse.builder()
                .userOld(userDtoOld)
                .userNew(updateUser)
                .build();
    }

    public ChangePasswordRequest changePassword(ChangePasswordRequest changePasswordRequest, JwtAuthenticationToken jwtToken) {
        String username = jwtToken.getName();
        User user = userRepo.findUserByUsername(username);

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Mật khẩu hiện tại không chính xác");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())) {
            throw new IllegalStateException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepo.save(user);
        return changePasswordRequest;
    }

    @Override
    public JwtResponse removeAuth(SignInFormRequest signInFormRequest) throws Exception {
        return null;
    }

}
