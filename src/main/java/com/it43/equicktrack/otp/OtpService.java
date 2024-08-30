package com.it43.equicktrack.otp;

import com.it43.equicktrack.exception.InvalidOtpException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;


    public boolean verifyEmailByCode(String code) throws InvalidOtpException{
        Otp emailOtp = otpRepository.findByCode(code).
                orElseThrow(() -> new ResourceNotFoundException("Otp code not found"));

        if(DateUtilities.isLate(emailOtp.getCreatedAt())) {
            throw new InvalidOtpException("Otp is expired");
        }

        User user = userRepository.findById(emailOtp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User.builder()
                .emailVerifiedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return true;
    }

    public void sendVerificationEmail(String email) {

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
        String otp = String.format("%06d", new Random().nextInt(999999));

        return otp;
    }



}
