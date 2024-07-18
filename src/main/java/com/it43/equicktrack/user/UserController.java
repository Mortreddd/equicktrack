package com.it43.equicktrack.user;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthenticatedUser authenticatedUser;
    @GetMapping
    public ResponseEntity<List<User>> getBorrowers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/{borrower_id}")

    public ResponseEntity<User> getBorrowerById(@PathVariable("borrower_id") Long _id){
        User user = userService.getBorrowerById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(path = "/{borrower_id}/delete")
    public ResponseEntity<User> deleteBorrowerById(@PathVariable("borrower_id") Long _id){
        return ResponseEntity.ok().body(userService.deleteBorrowerById(_id));
    }

    @PostMapping(path = "/me")
    public ResponseEntity<Authentication> getAuthenticatedUser(){
        return ResponseEntity.ok().body(AuthenticatedUser.getAuthenticatedUser());
    }

}
