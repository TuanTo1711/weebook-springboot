package org.weebook.api.service;


import org.weebook.api.dto.UserDto;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInRequest;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

public interface AuthService {
    JwtResponse login(SignInRequest signInRequest);

    UserDto register(SignUpRequest signUpRequest);

    UpdateProfileResponse updateProfile(UserDto userDto);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    JwtResponse removeAuth(SignInRequest signInRequest);

}
