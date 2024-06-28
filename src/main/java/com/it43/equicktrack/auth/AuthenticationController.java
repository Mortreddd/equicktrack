package com.it43.equicktrack.auth;

import com.it43.equicktrack.borrower.Borrower;
import com.it43.equicktrack.borrower.BorrowerService;
import com.it43.equicktrack.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtService jwtService;
    private final BorrowerService borrowerService;
    private final AuthenticationManager authenticationManager;


    @PostMapping(path = "/login")
    public ResponseEntity<String> authenticateAndGenerateToken(
            @RequestBody JwtLoginRequest jwtRequest
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
    public ResponseEntity<String> createBorrower(@RequestBody JwtRegisterRequest requestBorrower){
        borrowerService.createNewBorrower(requestBorrower);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jwtService.generateToken(requestBorrower.getEmail()));
    }

}
