package org.weebook.api.util;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OTPUtil {

    private static final long EXPIRATION_TIME_MINUTES = 15;
    private static final long MAX_REFRESH_COUNT = 3;

    private final RandomStringGenerator randomStringGenerator;
    private final ConcurrentHashMap<String, Integer> refreshCountMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Instant> expirationTimeMap = new ConcurrentHashMap<>();

    public OTPUtil() {
        this.randomStringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .filteredBy(Character::isDigit)
                .build();
    }

    public String generateOTP() {
        return randomStringGenerator.generate(4);
    }

    public boolean isOTPValid(String otp) {
        if (otp == null) {
            return false;
        }

        Instant expirationTime = expirationTimeMap.get(otp);
        return expirationTime != null && expirationTime.isAfter(Instant.now());
    }

    public String refreshOTP(String oldOTP) {
        if (!isOTPValid(oldOTP)) {
            return null;
        }

        int refreshCount = refreshCountMap.getOrDefault(oldOTP, 0);
        if (refreshCount >= MAX_REFRESH_COUNT) {
            return null;
        }

        String newOTP = generateOTP();
        refreshCountMap.put(oldOTP, refreshCount + 1);
        Instant now = Instant.now();
        Instant expirationTime = now.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES);
        expirationTimeMap.put(newOTP, expirationTime);
        return newOTP;
    }
}