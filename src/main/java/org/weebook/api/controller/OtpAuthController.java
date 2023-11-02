package org.weebook.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.service.impl.OTPServiceImpl;
import org.weebook.api.web.response.ResultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otp")
public class OtpAuthController {
    private final OTPServiceImpl otpService;

    @GetMapping("/verify-otp")
    public ResultResponse verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOtp(email, otp);
    }

    @PostMapping("/refresh-otp-expired")
    public ResultResponse refreshOtp(@RequestParam String email){
        otpService.refreshOtpExpired(email);
        return ResultResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh otp successfully please check email ^^")
                .data("Otp send mail you enter: "+email)
                .build();
    }
}
