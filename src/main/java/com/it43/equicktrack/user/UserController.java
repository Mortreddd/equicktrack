package com.it43.equicktrack.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/users", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> getUsers(){
        return ResponseEntity.ok("Hello World");
    }
}
