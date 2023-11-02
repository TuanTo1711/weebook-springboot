package org.weebook.api.service;

import org.weebook.api.web.response.ResultResponse;

public interface OTPService {
    void generateAndSendOTP(String username);

    String generateOTP();

    void sendOTPByEmail(String email, String otp);

    ResultResponse verifyOtp( String email , String otp);

    ResultResponse refreshOtpExpired(String email);

}
