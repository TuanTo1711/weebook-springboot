package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepo;
import org.weebook.api.service.OTPService;
import org.weebook.api.util.EmailSender;
import org.weebook.api.web.response.ResultResponse;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final UserRepo userRepo;
    private final EmailSender emailSender;

    @Override
    public void generateAndSendOTP(String username) {
        User user = userRepo.findUserByUsername(username);

        if (user != null) {
            String otp = generateOTP();
            user.setOtpCode(otp);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            System.out.println(user.getOtpExpiryTime());
            userRepo.save(user);
            emailSender.sendOTPByEmail(user.getEmail(), otp);
        }
    }

    @Override
    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    @Override
    public void sendOTPByEmail(String email, String otp) {

    }

    @Override
    public ResultResponse verifyOtp(String email, String otp) {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return ResultResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid Email !")
                    .build();
        }

        if (!user.getOtpCode().equals(otp)) {
            return ResultResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid OTP !")
                    .build();

        }
        if (!user.isEnabled()) {
            return ResultResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("OTP has expired")
                    .build();
        }

        user.setOtpCode(null);
        userRepo.save(user);
        return ResultResponse.builder()
                .data(HttpStatus.OK.value())
                .message("OTP verified successfully")
                .build();
    }

    @Override
    public ResultResponse refreshOtpExpired(String email) {
        User user = userRepo.findByEmail(email);
        if(user !=null && user.getOtpExpiryTime().isBefore(LocalDateTime.now())){
            String newOtp = generateOTP();
            user.setOtpCode(newOtp);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(15));
            userRepo.save(user);
            emailSender.sendOTPByEmail(email,newOtp);
        }
        return null;
    }


}
