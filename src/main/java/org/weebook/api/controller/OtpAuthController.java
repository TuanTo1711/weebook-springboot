package org.weebook.api.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.service.impl.OTPServiceImpl;
import org.weebook.api.web.response.ResultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
public class OtpAuthController {
    private final OTPServiceImpl otpService;

    @PostMapping("/verify-otp")
    public ResultResponse<String> verifyOTP(@RequestBody VerifyEmail body) {
        return ResultResponse.<String>builder()
                .status(200)
                .message("OTP Verify")
                .data(otpService.verifyOtp(body.getEmail(), body.getCode()))
                .build();
    }

    @GetMapping("/refresh-otp-expired")
    public ResultResponse refreshOtp(@RequestParam String email) {
        otpService.refreshOtpExpired(email);
        return ResultResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh otp successfully please check email ^^")
                .data("Otp send mail you enter: " + email)
                .build();
    }

    @Data
    public static class VerifyEmail {
        private String email;
        private String code;
    }
}
