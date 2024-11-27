package com.it43.equicktrack.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Constant {
    public static final long TIME_CHECK = 15_000L; // 30 seconds
    public static final long LATE_RETURN_MINUTES = 30; // 30 minutes
    public static final long OTP_EXPIRATION = 5;
    public static final long BEFORE_RETURN_MINUTES = 15;
    public static final String SMS_VERIFICATION_MESSAGE= "Your verification code is: %s. Please enter this code to verify your phone number. This code will expire in 5 minutes.";
    public static final String RETURN_NOTIFICATION_MESSAGE = "You were notified for the equipment, %s";
    public static final String BASE_URL = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    public static final long JWT_EXPIRATION_TIME = 604800000L;
}
