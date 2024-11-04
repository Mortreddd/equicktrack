package com.it43.equicktrack.auth;

import com.it43.equicktrack.dto.request.auth.ForgotPasswordRequest;
import com.it43.equicktrack.dto.request.OtpEmailRequest;
import com.it43.equicktrack.dto.request.auth.ResetPasswordRequest;
import com.it43.equicktrack.dto.request.auth.SmsVerificationRequest;
import com.it43.equicktrack.exception.EmailMessageException;
import com.it43.equicktrack.exception.InvalidOtpException;
import com.it43.equicktrack.otp.OtpService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.user.UserService;
import com.it43.equicktrack.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final OtpService otpService;

    @PostMapping(path = "/login", consumes = {"application/json"})
    public ResponseEntity<String> authenticateAndGenerateToken(@Validated @RequestBody JwtLoginRequestDTO jwtRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                )
        );

        log.info(jwtRequest.toString());
        
        if(authentication.isAuthenticated()){
            log.info("Logged in: {}", authentication.getDetails());
            return ResponseEntity.ok(jwtService.generateToken(jwtRequest.getEmail()));
        }
        else {
            log.error("Login failed: {}", authentication.getDetails());
            throw new UsernameNotFoundException("Credentials not found");
        }
    }

    @PostMapping(path = "/register", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<String> createBorrower(@Validated @RequestBody JwtRegisterRequestDTO requestUser) throws Exception {
        User newUser = userService.createUser(requestUser);
//        TODO: Uncomment this line of code after presentation
        otpService.sendEmailVerification(requestUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Email verification has been sent");
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(jwtService.generateToken(newUser.getEmail()));
    }

    @PutMapping(path = "/forgot-password/resend")
    public ResponseEntity<String> resendOtp(
            @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) throws EmailMessageException {
        otpService.resendForgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Verification otp code has been resent to the email");
    }

    @PostMapping(path = "/verify-email", consumes = "application/json")
    public ResponseEntity<String> verifyEmail(@Validated @RequestBody OtpEmailRequest otpEmailRequest) throws InvalidOtpException {
        String email = otpService.verifyEmailByCode(otpEmailRequest.getCode());
        return ResponseEntity.ok().body(jwtService.generateToken(email));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@Validated @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws EmailMessageException {

        otpService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Verification email is sent");
    }

    @GetMapping(path = "/verify-email/{uuid}")
    public ResponseEntity verifyEmailWithLink(
            @PathVariable("uuid") String uuid
    ) throws EmailMessageException, InvalidOtpException {
        if(otpService.verifyById(uuid)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @PatchMapping(path = "/reset-password/{otpUuid}")
    public ResponseEntity resetPassword(
            @PathVariable("otpUuid") String otpUuid,
            @Validated @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/verify-phone", consumes = {"application/json"})
    public ResponseEntity<String> verifyPhone(
            @Validated @RequestBody SmsVerificationRequest smsVerificationRequest
    ) {
        otpService.sendSmsVerification(smsVerificationRequest.getPhone());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Sms has been sent");
    }

/** TODO: Configure the generated User if the user chooses the google to authenticate
 *  TODO: Make a ModelRequest for the required attributes example, contactNumber, Role for the new user
 *
 * @param GoogleAuthenticationRequest
 * @return googleUid
 */
//    @PostMapping(path = "/google")
//    public ResponseEntity<String> authenticateUsingGoogle(@Validated @RequestBody GoogleAuthenticationRequest googleAuthenticationRequest) {
//        Optional<User> user = userService.getUserByUid(googleAuthenticationRequest.getUid());
//
//        if(user.isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(jwtService.generateToken(user.get().getEmail()));
//        }
//
//        String randomPassword = RandomStringUtils.randomAlphabetic(10);
//
//        User newUser = User.builder()
//                .email(googleAuthenticationRequest.getEmail())
//                .googleUid(googleAuthenticationRequest.getUid())
//                .photoUrl(googleAuthenticationRequest.getPhotoUrl())
//                .fullName(googleAuthenticationRequest.getDisplayName())
//                .password(new BCryptPasswordEncoder().encode(randomPassword))
//                .contactNumber()
//                .emailVerifiedAt(DateUtilities.now())
//                .build();
//
//        userRepository.save(newUser);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(jwtService.generateToken(newUser.getEmail()));
//    }

}