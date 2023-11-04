package org.weebook.api.util;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender mailSender;

    @Async
    public CompletableFuture<Void> sendOTPByEmail(String recipientEmail, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Mã OTP xác minh");
            helper.setText("Mã OTP của bạn là: " + otp);
            log.info("Mã OTP của bạn là: " + otp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        return CompletableFuture.completedFuture(null);
    }
}
