package org.weebook.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.UserDto;
import org.weebook.api.service.AuthService;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInFormRequest;
import org.weebook.api.web.request.SignUpFormRequest;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.ResultResponse;
import org.weebook.api.web.response.SignUpFormResponse;
import org.weebook.api.web.response.UpdateFormResponse;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-in")
    public ResultResponse<JwtResponse> signIn (@RequestBody @Valid SignInFormRequest signInFormRequest) throws Exception {
        JwtResponse jwtResponse = authService.loginAuth(signInFormRequest);
        return ResultResponse.<JwtResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login Successfully !")
                .data(jwtResponse)
                .build();
    }


    @PostMapping("sign-up")
    public ResultResponse<SignUpFormResponse> signUp (@RequestBody @Validated SignUpFormRequest signUpFormRequest) throws Exception {
        return ResultResponse.<SignUpFormResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Register Successfully !")
                .data(authService.signUpAuth(signUpFormRequest))
                .build();
    }

    @PutMapping("update/{id}")
    public ResultResponse<UpdateFormResponse> updateProfile (@PathVariable("id") Long userId, @RequestBody UserDto userDto) throws Exception {
        return ResultResponse.<UpdateFormResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Update Profile Successfully !")
                .data(authService.updateProfile(userDto, userId))
                .build();
    }


}
