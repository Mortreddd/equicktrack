package com.it43.equicktrack.profile;

import com.it43.equicktrack.dto.response.Response;
import com.it43.equicktrack.dto.user.SaveTokenRequest;
import com.it43.equicktrack.dto.user.UpdateProfilePasswordRequest;
import com.it43.equicktrack.dto.user.UpdateUserRequest;
import com.it43.equicktrack.dto.user.UserDTO;
import com.it43.equicktrack.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users/{userId}/profile")
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;

    @PutMapping(path = "/edit")
    public ResponseEntity<UserDTO> editProfile(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserProfile(userId, request));
    }

    @PatchMapping(path = "/edit/password")
    public ResponseEntity<Response> editProfilePassword(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody UpdateProfilePasswordRequest updateProfilePasswordRequest
    ) {
        userService.updateProfilePassword(userId, updateProfilePasswordRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully changed password")
                        .build()
                );
    }

    @PutMapping(path = "/save-token")
    public ResponseEntity<Response> saveToken(
            @PathVariable("userId") Long userId,
            @RequestBody SaveTokenRequest saveTokenRequest
    ) {
        profileService.saveToken(userId, saveTokenRequest.getToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Token has been saved")
                        .build()
                );
    }

//    @PostMapping(path = "/logout")
//    public ResponseEntity<Response> logoutProfile(
//            @PathVariable("userId") Long userId
//    ) {
//        return
//    }
}
