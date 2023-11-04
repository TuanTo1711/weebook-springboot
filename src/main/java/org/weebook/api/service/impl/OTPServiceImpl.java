package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.service.OTPService;
import org.weebook.api.util.EmailSender;
import org.weebook.api.web.response.ResultResponse;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    @Override
    public void generateAndSendOTP(String username) {
        var optional = userRepository.findByUsername(username);

        if (optional.isPresent()) {
            User user = optional.get();
            String otp = generateOTP();
            user.setOtpCode(otp);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            userRepository.saveAndFlush(user);
            emailSender.sendOTPByEmail(user.getEmail(), otp);
        }
    }

    @Override
    public String generateOTP() {

        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10);
            otpBuilder.append(randomNumber);
        }
        return otpBuilder.toString();
    }

    @Override
    public void sendOTPByEmail(String email, String otp) {

    }

    @Override
    public String verifyOtp(String email, String otp) {
        var optional = userRepository.findByEmail(email);

        if (optional.isEmpty()) {
            return "Invalid Email !";
        }

        User user = optional.get();
        if (!user.getOtpCode().equals(otp)) {
            return "Invalid OTP !";

        }
        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            return "OTP Expired";
        }

        user.setOtpCode(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);
        return "OTP verified successfully";
    }

    @Override
    public ResultResponse refreshOtpExpired(String email) {
        var optional = userRepository.findByEmail(email);
        if (optional.isPresent() && optional.get().getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            User user = optional.get();
            String newOtp = generateOTP();
            user.setOtpCode(newOtp);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);
            emailSender.sendOTPByEmail(email, newOtp);
        }
        return null;
    }


}
