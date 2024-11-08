package com.it43.equicktrack.auth;

import com.it43.equicktrack.dto.auth.JwtLoginRequest;
import com.it43.equicktrack.dto.auth.JwtRegisterRequest;
import com.it43.equicktrack.dto.auth.JwtToken;
import com.it43.equicktrack.dto.request.auth.ForgotPasswordRequest;
import com.it43.equicktrack.dto.request.VerifyEmailRequest;
import com.it43.equicktrack.dto.request.auth.ResetPasswordRequest;
import com.it43.equicktrack.dto.request.auth.SmsVerificationRequest;
import com.it43.equicktrack.dto.response.Response;
import com.it43.equicktrack.exception.EmailMessageException;
import com.it43.equicktrack.exception.auth.InvalidOtpException;
import com.it43.equicktrack.otp.OtpService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.user.UserService;
import com.it43.equicktrack.jwt.JwtService;
import com.it43.equicktrack.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.it43.equicktrack.util.Constant.JWT_EXPIRATION_TIME;

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
    public ResponseEntity<JwtToken> authenticateAndGenerateToken(@Validated @RequestBody JwtLoginRequest jwtRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                )
        );

        log.info(jwtRequest.toString());
        
        if(authentication.isAuthenticated()){
            String accessToken = jwtService.generateToken(jwtRequest.getEmail());

            JwtToken jwtToken = JwtToken
                    .builder()
                    .accessToken(accessToken)
                    .iat(new Date(System.currentTimeMillis()).getTime())
                    .iss(Constant.BASE_URL)
                    .exp(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME).getTime())
                    .build();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(jwtToken);
        }
        else {
            log.error("Login failed: {}", authentication.getDetails());
            throw new UsernameNotFoundException("Credentials not found");
        }
    }

    @PostMapping(path = "/register", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<JwtToken> createBorrower(@Validated @RequestBody JwtRegisterRequest requestUser) throws Exception {
        User newUser = userService.createUser(requestUser);
        otpService.sendEmailVerification(requestUser.getEmail());
        String accessToken = jwtService.generateToken(newUser.getEmail());
        JwtToken jwtToken = JwtToken.builder()
                .iss(Constant.BASE_URL)
                .iat(new Date(System.currentTimeMillis()).getTime())
                .exp(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME).getTime())
                .accessToken(accessToken)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jwtToken);
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
    public ResponseEntity<Response> verifyEmail(@Validated @RequestBody VerifyEmailRequest verifyEmailRequest) throws InvalidOtpException, EmailMessageException {
        otpService.sendChangeEmailVerification(verifyEmailRequest.getOldEmail(), verifyEmailRequest.getNewEmail());
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(200)
                        .message(String.format("Email verification %s has been sent", verifyEmailRequest.getNewEmail()))
                        .build()
                );
    }

//    @PutMapping(path = "/verify-email/change")
//    public ResponseEntity<Response> changeVerifyEmail(@Validated @RequestBody VerifyEmailRequest verifyEmailRequest) throws InvalidOtpException, EmailMessageException {
//        otpService.sendEmailVerification(verifyEmailRequest.getEmail());
//        return ResponseEntity.ok()
//                .body(Response.builder()
//                        .code(200)
//                        .message(String.format("Email verification %s has been sent", verifyEmailRequest.getEmail()))
//                        .build()
//                );
//    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@Validated @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws EmailMessageException {

        otpService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Verification email is sent");
    }

    @GetMapping(path = "/verify-email/{uuid}")
    public ResponseEntity<Response> verifyEmailWithLink(
            @PathVariable("uuid") String uuid
    ) throws EmailMessageException {
        otpService.verifyByUuid(uuid);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(200)
                        .message("Successfully verified")
                        .build()
                );
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


    @GetMapping(path = "/me")
    public ResponseEntity<Object> verifyJwtToken() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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