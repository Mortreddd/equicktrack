package com.it43.equicktrack.test;

import com.it43.equicktrack.exception.auth.EmailExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testEndpoint() throws ExpiredJwtException {
        throw new ExpiredJwtException(null, null, "Jwt token is expired");
    }
}
