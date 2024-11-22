package com.it43.equicktrack.otp;

import com.it43.equicktrack.dto.request.auth.ResetPasswordRequest;
import com.it43.equicktrack.email.EmailService;
import com.it43.equicktrack.exception.EmailMessageException;
import com.it43.equicktrack.exception.auth.InvalidOtpException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.contact.ContactService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ContactService contactService;

    public void sendSmsVerification(Long userId, String phone) {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User %s not found", userId.toString())
                ));

        otpRepository.findByUserId(userId).ifPresent(otpRepository::delete);

        Otp otp = otpRepository.save(
                Otp.builder()
                .id(RANDOM_ID)
                .contactNumber(phone)
                .email(null)
                .userId(userId)
                .code(OTP_CODE)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build()
        );

        otpRepository.save(otp);

        contactService.sendVerificationCode(phone, otp.getCode());
    }

    public void verifyPhoneByOtp(String otpCode) {

        Otp otp = otpRepository.findByCode(otpCode)
                .orElseThrow(() -> new ResourceNotFoundException("Otp code is invalid"));

        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s is not found", otp.getUserId().toString())));

        if(DateUtilities.isLate(otp.getCreatedAt())) {
            throw new InvalidOtpException("Otp code is expired");
        }

        user.setContactNumber(otp.getContactNumber());
        user.setContactNumberVerifiedAt(DateUtilities.now());
        user.setUpdatedAt(DateUtilities.now());
        userRepository.save(user);
        otpRepository.delete(otp);

    }

    public void sendEmailVerification(String email) throws EmailMessageException {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User email not found"));

        otpRepository.findByEmail(email)
                .ifPresent(otpRepository::delete);

        Otp newOtp = Otp.builder()
                .id(RANDOM_ID)
                .userId(existingUser.getId())
                .email(email)
                .contactNumber(null)
                .code(OTP_CODE)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build();

        otpRepository.save(newOtp);
        emailService.sendVerifyEmail(email, newOtp.getId());
    }

    public void sendChangeEmailVerification(String oldEmail, String newEmail) throws EmailMessageException {
        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();
        User existingUser = userRepository.findByEmail(oldEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User email not found"));

        if(!existingUser.getEmail().equals(newEmail)) {
            existingUser.setEmail(newEmail);
            existingUser.setEmailVerifiedAt(null);
            userRepository.save(existingUser);
        }

        otpRepository.findByEmail(oldEmail).ifPresent(otpRepository::delete);

        Otp newOtp = otpRepository.save(Otp.builder()
                .id(RANDOM_ID)
                .userId(existingUser.getId())
                .email(newEmail)
                .contactNumber(null)
                .code(OTP_CODE)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build()
        );

        emailService.sendVerifyEmail(newEmail, newOtp.getId());
    }

    public void verifyByUuid(String otpUuid) throws InvalidOtpException {
        Optional<Otp> currentOtp = otpRepository.findById(otpUuid);

        if(currentOtp.isEmpty()) {
            throw new ResourceNotFoundException("Otp not found");
        }

        Otp otp = currentOtp.get();

        if(DateUtilities.isExpired(otp.getCreatedAt())) {
            throw new InvalidOtpException("Url verification is expired");
        }

        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmailVerifiedAt(DateUtilities.now());
        user.setUpdatedAt(DateUtilities.now());
        userRepository.save(user);
        otpRepository.delete(otp);
    }

    public void forgotPassword(String email) throws EmailMessageException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s doesn't match our credentials", email)));

        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

        otpRepository.findByUserId(user.getId()).ifPresent(otpRepository::delete);

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
        emailService.sendResetPassword(email, otp.getId());
    }

    public void verifyForgotPasswordByUuid(String uuid) {
        Otp otp = otpRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid otp url"));

        if(DateUtilities.isLate(otp.getCreatedAt())) {
            throw new InvalidOtpException("Reset password url is expired");
        }
    }

    public void sendForgotPassword(String email) throws EmailMessageException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s doesn't match credentials", email)));

        otpRepository.findByUserId(user.getId()).ifPresent(otpRepository::delete);

        final String OTP_CODE = generateRandomOtpCode();
        final String RANDOM_ID = UUID.randomUUID().toString();

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
        emailService.sendResetPassword(email, newOtp.getId());
    }

    public void resetPassword(String otpUuid, ResetPasswordRequest resetPasswordRequest) {
        Otp otp = otpRepository.findById(otpUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Otp is invalid"));

        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordRequest.getPassword()));
        user.setUpdatedAt(DateUtilities.now());
        userRepository.save(user);
        otpRepository.delete(otp);
    }


//    Generate a 6 digit code ex: 342130
    public String generateRandomOtpCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }


}
