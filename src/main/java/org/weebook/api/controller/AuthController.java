package org.weebook.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.UserDto;
import org.weebook.api.service.AuthService;
import org.weebook.api.web.request.ChangePasswordRequest;
import org.weebook.api.web.request.SignInRequest;
import org.weebook.api.web.request.SignUpRequest;
import org.weebook.api.web.request.VerifyEmail;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.ResultResponse;
import org.weebook.api.web.response.UpdateProfileResponse;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-in")
    public ResultResponse<JwtResponse> signIn(@RequestBody @Validated SignInRequest signInRequest) {
        JwtResponse jwtResponse = authService.login(signInRequest);
        return ResultResponse.<JwtResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login successfully !")
                .data(jwtResponse)
                .build();
    }

    @PostMapping("sign-up")
    public ResultResponse<UserDto> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        UserDto registered = authService.register(signUpRequest);
        return ResultResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .message("Register successfully !")
                .data(registered)
                .build();
    }

    @PutMapping("profile/update")
    public ResultResponse<UpdateProfileResponse> updateProfile(@RequestBody @Validated UserDto userDto) {
        return ResultResponse.<UpdateProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Update profile successfully !")
                .data(authService.update(userDto))
                .build();
    }

    @PatchMapping("/recovery-password")
    public ResultResponse<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return ResultResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Update password successfully please login again !")
                .data(null)
                .build();
    }

    @PostMapping("/verify-otp")
    public ResultResponse<Boolean> verifyOTP(@RequestBody VerifyEmail body) {
        return ResultResponse.<Boolean>builder()
                .status(200)
                .message("OTP Verify")
                .data(authService.verifyOtp(body.email(), body.code()))
                .build();
    }
}
