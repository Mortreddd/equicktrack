package com.it43.equicktrack.auth;

import com.it43.equicktrack.dto.auth.JwtLoginRequest;
import com.it43.equicktrack.dto.auth.JwtRegisterRequest;
import com.it43.equicktrack.dto.auth.JwtToken;
import com.it43.equicktrack.dto.request.auth.ForgotPasswordRequest;
import com.it43.equicktrack.dto.request.VerifyEmailRequest;
import com.it43.equicktrack.dto.request.auth.ResetPasswordRequest;
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
    public ResponseEntity<JwtToken> authenticateAndGenerateToken(@Validated @RequestBody JwtLoginRequest jwtRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                )
        );

        log.info(jwtRequest.toString());

        if (authentication.isAuthenticated()) {
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
        } else {
            log.error("Login failed: {}", authentication.getDetails());
            throw new UsernameNotFoundException("Credentials not found");
        }
    }

    @PostMapping(path = "/register", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<JwtToken> createBorrower(@Validated @RequestBody JwtRegisterRequest requestUser) throws Exception {
        User newUser = userService.createUser(requestUser);
        otpService.sendEmailVerification(newUser.getEmail());
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

    @PostMapping(path = "/forgot-password", consumes = "application/json")
    public ResponseEntity<Response> forgotPassword(
            @Validated @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) throws EmailMessageException {

        otpService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Reset password request was sent")
                        .build()
                );
    }

    @GetMapping(path = "/forgot-password/{uuid}")
    public ResponseEntity<Response> verifyForgotPassword(
            @PathVariable("uuid") String uuid
    ) {

        otpService.verifyForgotPasswordByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully verified the reset password url")
                        .build()
                );
    }

    @PatchMapping(path = "/reset-password/{uuid}", consumes = "application/json")
    public ResponseEntity<Response> forgotPasswordVerify(
            @PathVariable("uuid") String uuid,
            @Validated @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        otpService.resetPassword(uuid, resetPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully reset password")
                        .build()
                );
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

    @GetMapping(path = "/me")
    public ResponseEntity<Object> verifyJwtToken() {    
        return ResponseEntity.status(HttpStatus.OK)
                .body(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    }
}