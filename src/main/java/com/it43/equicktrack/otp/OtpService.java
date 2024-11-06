package com.it43.equicktrack.otp;

import com.it43.equicktrack.email.EmailService;
import com.it43.equicktrack.exception.EmailMessageException;
import com.it43.equicktrack.exception.auth.InvalidOtpException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.sms.SmsService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SmsService smsService;
//    returns email
    public String verifyEmailByCode(String code) throws InvalidOtpException{
        log.debug(code);
        Otp emailOtp = otpRepository.findByCode(code).
                orElseThrow(() -> new ResourceNotFoundException("Otp code not found"));

        if(DateUtilities.isLate(emailOtp.getUpdatedAt())) {
            throw new InvalidOtpException("Otp is expired");
        }

        User user = userRepository.findById(emailOtp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmailVerifiedAt(DateUtilities.now());
        userRepository.save(user);
        otpRepository.delete(emailOtp);
        return user.getEmail();
    }

    public void sendSmsVerification(String phone) {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

        smsService.sendVerificationCode(phone, OTP_CODE);
    }

    public void sendEmailVerification(String email) throws EmailMessageException {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();
        Otp otp = null;
//        CHECK FOR THE EXISTING OTP CODE
        Optional<Otp> existingOtp = otpRepository.findByEmail(email);

        if(existingOtp.isEmpty()) {
            User newUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User email not found"));
            otp = Otp.builder()
                    .id(RANDOM_ID)
                    .userId(newUser.getId())
                    .email(email)
                    .contactNumber(null)
                    .code(OTP_CODE)
                    .createdAt(DateUtilities.now())
                    .updatedAt(DateUtilities.now())
                    .build();
        } else {
            otp = existingOtp.get();
            otp.setCode(OTP_CODE);
            otp.setCreatedAt(DateUtilities.now());
            otp.setUpdatedAt(DateUtilities.now());

        }


        otpRepository.save(otp);
        emailService.sendVerifyEmail(email, OTP_CODE, otp.getId());
    }

    public boolean verifyById(String otpUuid) throws InvalidOtpException {
        Otp otp = otpRepository.findById(otpUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Otp not found"));

        if(DateUtilities.isExpired(otp.getCreatedAt())) {
            throw new InvalidOtpException("Verification is expired");
        }

        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmailVerifiedAt(DateUtilities.now());
        user.setUpdatedAt(DateUtilities.now());
        userRepository.save(user);
        otpRepository.delete(otp);
        return true;
    }

    public void forgotPassword(String email) throws EmailMessageException {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Otp otp = Otp.builder()
                .id(RANDOM_ID)
                .code(OTP_CODE)
                .userId(user.getId())
                .email(email)
                .contactNumber(null)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build();

        otpRepository.save(otp);
        emailService.sendResetPassword(email, OTP_CODE, otp.getId());
    }

    public void resendForgotPassword(String email) throws EmailMessageException {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Otp otp = otpRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User did not request a otp"));

        otpRepository.delete(otp);

        Otp newOtp = Otp.builder()
                .id(RANDOM_ID)
                .code(OTP_CODE)
                .userId(user.getId())
                .email(email)
                .contactNumber(null)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build();


        otpRepository.save(newOtp);
        emailService.sendResetPassword(email, OTP_CODE, newOtp.getId());
    }


    public boolean resetPassword(String otpUuid, String password) {
        Otp otp = otpRepository.findById(otpUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Otp not found"));

        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setUpdatedAt(DateUtilities.now());
        otpRepository.delete(otp);

        return true;
    }


    public boolean isValidCode(String code) {
        return otpRepository.codeExists(code);
    }


    public List<Otp> getOtps() {
        return otpRepository.findAll();
    }


    public void deleteAll(List<Otp> otps) {
        otpRepository.deleteAll(otps);
    }

//    Generate a 6 digit code ex: 342130
    public String generateRandomOtpCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }


}
