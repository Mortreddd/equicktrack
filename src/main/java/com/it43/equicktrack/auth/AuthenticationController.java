package com.it43.equicktrack.auth;

import com.it43.equicktrack.dto.request.GoogleAuthenticationRequestDTO;
import com.it43.equicktrack.dto.request.OtpEmailRequestDTO;
import com.it43.equicktrack.exception.InvalidOtpException;
import com.it43.equicktrack.exception.InvalidTokenException;
import com.it43.equicktrack.otp.OtpService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.user.UserService;
import com.it43.equicktrack.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @PostMapping(path = "/login")
    public ResponseEntity<String> authenticateAndGenerateToken(
            @RequestBody JwtLoginRequestDTO jwtRequest
    ){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                )
        );


        if(authentication.isAuthenticated()){
            log.info("Logged in: {}", authentication.getDetails());
            return ResponseEntity.ok(jwtService.generateToken(jwtRequest.getEmail()));
        }
        else {
            log.error("Login failed: {}", authentication.getDetails());
            throw new UsernameNotFoundException("Credentials not found");
        }
    }


    @PostMapping(path = "/register")
    public ResponseEntity<String> createBorrower(@RequestBody JwtRegisterRequestDTO requestUser) throws Exception {
        userService.createUser(requestUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jwtService.generateToken(requestUser.getEmail()));
    }

    @PostMapping(path = "/verify-email")
    public ResponseEntity<String> verifyEmail(OtpEmailRequestDTO otpEmailRequestDTO) throws InvalidOtpException {

        if(!otpService.verifyEmailByCode(otpEmailRequestDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Otp code not found");
        }

        return ResponseEntity.ok().body("Successfully verified");
    }

    @PostMapping(path = "/google")
    public ResponseEntity<String> authenticateUsingGoogle(@ModelAttribute GoogleAuthenticationRequestDTO googleAuthenticationRequestDTO) {
        Optional<User> user = userService.getUserByUid(googleAuthenticationRequestDTO.getUid());

        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jwtService.generateToken(user.get().getEmail()));
        }

        String randomPassword = RandomStringUtils.randomAlphabetic(10);

        User newUser = User.builder()
                .email(googleAuthenticationRequestDTO.getEmail())
                .googleUid(googleAuthenticationRequestDTO.getUid())
                .photoUrl(googleAuthenticationRequestDTO.getPhotoUrl())
                .fullName(googleAuthenticationRequestDTO.getDisplayName())
                .password(new BCryptPasswordEncoder().encode(randomPassword))
                .emailVerifiedAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(jwtService.generateToken(newUser.getEmail()));
    }

}