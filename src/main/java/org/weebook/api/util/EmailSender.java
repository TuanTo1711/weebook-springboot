package org.weebook.api.util;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {
    private final JavaMailSender mailSender = new JavaMailSenderImpl();

    @Async
    public void sendOTPEmail(String toEmail, String otpCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setFrom("no-reply");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Mã OTP của bạn");
            messageHelper.setText("Mã OTP của bạn là: " + otpCode);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Xử lý lỗi gửi email
            e.printStackTrace();
        }
    }
}
