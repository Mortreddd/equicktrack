package com.it43.equicktrack.user;

import com.it43.equicktrack.dto.user.UpdateUserDTO;
import com.it43.equicktrack.dto.user.UserTransactionDTO;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticatedUser authenticatedUser;
    private final TransactionService transactionsService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long _id){
        User user = userService.getUserById(_id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(path = "/{userId}/delete")
    public ResponseEntity<User> deleteUserById(@PathVariable("userId") Long _id){

        return ResponseEntity.ok().body(userService.deleteUserById(_id));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<Object> getAuthenticatedUser(){
        return ResponseEntity.ok().body(AuthenticatedUser.getAuthenticatedUser());
    }

    @GetMapping(path = "/{userId}/transactions")
    public ResponseEntity<UserTransactionDTO> getUserTransactions(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionsService.getTransactionsByUser(userId));
    }

    @PatchMapping(path = "/{userId}/update")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UpdateUserDTO updateUser) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(userId, updateUser));
    }
}
