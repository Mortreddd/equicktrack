package com.it43.equicktrack.user;

import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UpdateUserRoleRequest;
import com.it43.equicktrack.dto.user.UserDTO;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ){
        return ResponseEntity.ok().body(userService.getUsers(pageNo, pageSize));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long _id){
        User user = userService.getUserById(_id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping(path = "/{userId}/update")
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserRoleRequest updateUserRoleRequest
    ) {
        return ResponseEntity.ok()
                .body(userService.updateUser(userId, updateUserRoleRequest));
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
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionsService.getTransactionsByUser(userId));
    }


}
