package org.weebook.api.service;


import org.weebook.api.dto.UserDto;
import org.weebook.api.web.request.SignInFormRequest;
import org.weebook.api.web.request.SignUpFormRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.SignUpFormResponse;
import org.weebook.api.web.response.UpdateFormResponse;

public interface AuthService {
    JwtResponse loginAuth(SignInFormRequest signInFormRequest) throws Exception;
    SignUpFormResponse signUpAuth(SignUpFormRequest signUpFormRequest) throws Exception;

    UpdateFormResponse updateProfile(UserDto userDto, Long id);

    JwtResponse removeAuth(SignInFormRequest signInFormRequest) throws Exception;

}
